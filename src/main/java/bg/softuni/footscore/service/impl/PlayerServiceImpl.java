package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.ResponsePlayerDetailsApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerStatisticsApiDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.PlayerTeamSeason;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.repository.PlayerRepository;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.PlayerTeamSeasonService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.TeamService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final PlayerTeamSeasonService playerTeamSeasonService;
    private final ModelMapper modelMapper;
    private final ApiConfig apiConfig;
    private final RestClient restClient;

    public static final String PLAYERS_BY_TEAM_SEASON_AND_PAGE = "%splayers?team=%d&season=%d&page=%d";
    public static final String PLAYERS_BY_ID = "%splayers/squads?player=%d";

    public PlayerServiceImpl(PlayerRepository playerRepository,
                             TeamService teamService,
                             SeasonService seasonService,
                             PlayerTeamSeasonService seasonTeamPlayerService,
                             ModelMapper modelMapper,
                             ApiConfig apiConfig,
                             RestClient restClient) {
        this.playerRepository = playerRepository;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.playerTeamSeasonService = seasonTeamPlayerService;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
    }


    @Override
    @Transactional
    public void saveApiPlayersForTeamAndSeason(TeamPageDto team, SeasonPageDto season) {

        ResponsePlayerApiDto responseList = this.getResponsePlayerApiDto(PLAYERS_BY_TEAM_SEASON_AND_PAGE, team.getApiId(), season.getYear(), 1);

        if (!responseList.getResponse().isEmpty()) {
            for (int i = 1; i <= responseList.getPaging().getTotal(); i++) {
                if (i != 1) {
                    responseList = this.getResponsePlayerApiDto(PLAYERS_BY_TEAM_SEASON_AND_PAGE, team.getApiId(), season.getYear(), i);
                }

                List<Player> playersToSave = new ArrayList<>();
                responseList.getResponse().forEach(dto -> {
                    if (this.getPlayerByApiId(dto.getPlayer().getId()) == null) {
                        Player player = createPlayer(dto);

                        Optional<Player> optional = this.playerRepository.findByApiId(player.getApiId());

                        if (optional.isEmpty()) {
                            player.setPosition(dto.getStatistics().getFirst().getGames().getPosition());
                            playersToSave.add(player);
                        }
                    }
                });
                playersToSave.forEach(player -> {
                    Optional<Player> optional = this.playerRepository.findByApiId(player.getApiId());

                    optional.ifPresent(playersToSave::remove);
                });

                if (!playersToSave.isEmpty()) {
                    this.playerRepository.saveAll(playersToSave);
                }

                responseList.getResponse().forEach(dto -> {
                    Optional<Player> player = this.playerRepository.findByApiId(dto.getPlayer().getId());
                    if (player.isPresent()) {
                        List<PlayerTeamSeasonPageDto> list = this.playerTeamSeasonService.getByTeamIdAndSeasonId(team.getId(), season.getId());
                        List<PlayerPageDto> map = list.stream().map(s -> {
                            if (s.getPlayer().getId() == player.get().getId()) {
                                return s.getPlayer();
                            } else {
                                return null;
                            }
                        }).toList();

                        if (map.isEmpty() || map.getFirst() == null) {
                            PlayerTeamSeason seasonTeamPlayer = new PlayerTeamSeason();
                            seasonTeamPlayer.setSeason(this.modelMapper.map(season, Season.class));
                            seasonTeamPlayer.setTeam(this.modelMapper.map(team, Team.class));
                            seasonTeamPlayer.setPlayer(player.get());

                            this.playerTeamSeasonService.save(seasonTeamPlayer);
                        }
                    }
                });
            }
        }
    }


    @Override
    public ResponsePlayerApiDto getResponsePlayerApiDto(String query, Long id, Integer seasonYear, Integer page) {
        if (query == null || query.isEmpty() || id == null || seasonYear == null || page == null) {
            throw new IllegalArgumentException("Invalid query/id/season year or page parameters");
        }
        String url = String.format(query, this.apiConfig.getUrl(), id, seasonYear, page);

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(ResponsePlayerApiDto.class);
    }

    @Override
    public ResponsePlayerDetailsApiDto getResponsePlayerDetailsApiDto(String query, Long playerId) {
        if (query == null || query.isEmpty() || playerId == null) {
            throw new IllegalArgumentException("Invalid query or id parameters");
        }
        String url = String.format(query, this.apiConfig.getUrl(), playerId);

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(ResponsePlayerDetailsApiDto.class);
    }

    @Override
    public PlayerPageDto getPlayerByApiId(Long apiId) {
        return this.playerRepository.findByApiId(apiId)
                .map(p -> this.modelMapper.map(p, PlayerPageDto.class))
                .orElse(null);
    }

    @Override
    public void fillMissingPlayerDetails(Long playerApiId) {
        if (playerApiId == null) {
            throw new IllegalArgumentException("Invalid player api id");
        }
        Optional<Player> optionalPlayer = this.playerRepository.findById(playerApiId);
        if (optionalPlayer.isPresent()) {
            ResponsePlayerDetailsApiDto response = getResponsePlayerDetailsApiDto(PLAYERS_BY_ID, optionalPlayer.get().getApiId());

            if (response.getResponse().isEmpty()) {
                optionalPlayer.get().setRetired(true);
                optionalPlayer.get().setAge(this.seasonService.getAllSeasons().getLast().getYear() - optionalPlayer.get().getBirthday().getYear());
            } else {
                response.getResponse().forEach(dto -> {
                    TeamPageDto optionalTeam = this.teamService.getTeamByApiId(dto.getTeam().getId());
                    if (optionalTeam != null) {
                        optionalPlayer.get().setTeam(optionalTeam.getName());
                    }
                    optionalPlayer.get().setRetired(false);
                    optionalPlayer.get().setNumber(dto.getPlayers().getFirst().getNumber());
                });
            }
            this.playerRepository.save(optionalPlayer.get());
        }
    }

    @Override
    public boolean isEmpty() {
        return this.playerRepository.count() == 0;
    }

    @Override
    public PlayerPageDto getPlayerById(Long playerId) {
        return this.playerRepository.findById(playerId)
                .map(p -> this.modelMapper.map(p, PlayerPageDto.class))
                .orElse(null);
    }

    @Override
    public List<PlayerPageDto> getAllByIds(List<Long> playerIds) {
        return this.playerRepository.findAllById(playerIds)
                .stream()
                .map(p -> this.modelMapper.map(p, PlayerPageDto.class))
                .toList();
    }

    @Override
    public List<PlayerPageDto> getAllPlayers() {
        return this.playerRepository.findAll().stream().map(p -> this.modelMapper.map(p, PlayerPageDto.class)).toList();
    }

    @Override
    public List<PlayerPageDto> getAllSelectedPlayers(boolean bool) {
        return this.playerRepository.findByIsSelected(bool).stream().map(p -> this.modelMapper.map(p, PlayerPageDto.class)).toList();
    }

    @Override
    public void setSelected(Long playerId, boolean b) {
        Optional<Player> optionalPlayer = this.playerRepository.findById(playerId);
        if (optionalPlayer.isPresent()) {
            optionalPlayer.get().setSelected(b);
            updatePlayer(optionalPlayer.get());
        }
    }

    private void updatePlayer(Player player) {
        this.playerRepository.save(player);
    }

    private static Player createPlayer(PlayerStatisticsApiDto dto) {
        String heightStr = dto.getPlayer().getHeight();
        String weightStr = dto.getPlayer().getWeight();

        Integer height = null;
        Integer weight = null;

        height = parseStringToInteger(heightStr, height);

        weight = parseStringToInteger(weightStr, weight);

        LocalDate birthDate = parseBirthDate(dto.getPlayer().getBirth().getDate());

        if (birthDate != null) {
            System.out.println("Parsed date: " + birthDate);
        } else {
            System.out.println("Failed to parse date");
        }

        return new Player(
                dto.getPlayer().getFirstname(),
                dto.getPlayer().getLastname(),
                dto.getPlayer().getName(),
                dto.getPlayer().getAge(),
                birthDate,
                dto.getPlayer().getNationality(),
                height,
                weight,
                dto.getPlayer().getPhoto(),
                dto.getPlayer().getId());
    }

    private static Integer parseStringToInteger(String heightStr, Integer height) {
        if (heightStr != null && !heightStr.trim().isEmpty()) {
            String[] heightParts = heightStr.split("\\s+");
            if (heightParts.length > 0) {
                height = Integer.parseInt(heightParts[0]);
            }
        }
        return height;
    }

    private static LocalDate parseBirthDate(String dateString) {

        if (dateString == null) {
            return null;
        }

        DateTimeFormatter standardFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fallbackFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        try {
            return LocalDate.parse(dateString, standardFormatter);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(dateString, fallbackFormatter);
            } catch (DateTimeParseException e2) {
                System.err.println("Error parsing date: " + e2.getMessage());
                return null;
            }
        }
    }
}
