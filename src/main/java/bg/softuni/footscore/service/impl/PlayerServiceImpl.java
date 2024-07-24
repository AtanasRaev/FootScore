package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.PlayerStatisticsApiDto;
import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.SeasonsByPlayerApiDto;
import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.SeasonTeamPlayer;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.repository.PlayerRepository;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.SeasonTeamPlayerService;
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
    private final SeasonTeamPlayerService seasonTeamPlayerService;
    private final ModelMapper modelMapper;
    private final ApiConfig apiConfig;
    private final RestClient restClient;

    public static final String PLAYERS_BY_TEAM_SEASON_AND_PAGE = "%splayers?team=%d&season=%d&page=%d";
    public static final String PLAYERS_BY_ID_AND_SEASON = "%splayers?id=%d&season=%d";
    public static final String PLAYERS_BY_ID_AND_ALL_SEASONS = "%splayers/seasons?player=%d";

    public PlayerServiceImpl(PlayerRepository playerRepository,
                             TeamService teamService,
                             SeasonService seasonService,
                             SeasonTeamPlayerService seasonTeamPlayerService,
                             ModelMapper modelMapper,
                             ApiConfig apiConfig,
                             RestClient restClient) {
        this.playerRepository = playerRepository;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.seasonTeamPlayerService = seasonTeamPlayerService;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
    }


    @Override
    @Transactional
    public void saveApiPlayersForTeamAndSeason(Team team, Season season) {

        ResponsePlayerApiDto responseList = this.getResponsePlayerApiDto(PLAYERS_BY_TEAM_SEASON_AND_PAGE, team.getApiId(), season.getYear(), 1);

        if (!responseList.getResponse().isEmpty()) {
            for (int i = 1; i <= responseList.getPaging().getTotal(); i++) {
                if (i != 1) {
                    responseList = this.getResponsePlayerApiDto(PLAYERS_BY_TEAM_SEASON_AND_PAGE, team.getApiId(), season.getYear(), i);
                }

                List<Player> playersToSave = new ArrayList<>();
                responseList.getResponse().forEach(dto -> {
                    if (this.getPlayerByApiId(dto.getPlayer().getId()).isEmpty()) {
                        Player player = createPlayer(dto);
                        player.setPosition(dto.getStatistics().getFirst().getGames().getPosition());
                        playersToSave.add(player);
                    }
                });
                this.playerRepository.saveAll(playersToSave);

                responseList.getResponse().forEach(dto -> {
                    Optional<Player> player = this.playerRepository.findByApiId(dto.getPlayer().getId());
                    if (player.isPresent()) {
                        Optional<Player> optionalPlayer = this.seasonTeamPlayerService.getPlayerByTeamIdAndSeasonId(team.getId(), season.getId(), player.get().getId());
                        if (optionalPlayer.isEmpty()) {
                            SeasonTeamPlayer seasonTeamPlayer = new SeasonTeamPlayer();
                            seasonTeamPlayer.setSeason(season);
                            seasonTeamPlayer.setTeam(team);
                            seasonTeamPlayer.setPlayer(player.get());

                            this.seasonTeamPlayerService.save(seasonTeamPlayer);
                        }
                    }
                });
            }
        }
    }


    @Override
    public ResponsePlayerApiDto getResponsePlayerApiDto(String query, long id, int seasonYear, int page) {
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
    public SeasonsByPlayerApiDto getResponseSeasonsByPlayerApiDto(String query, long id) {
        String url = String.format(query, this.apiConfig.getUrl(), id);

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(SeasonsByPlayerApiDto.class);
    }

    @Override
    public Optional<Player> getPlayerByApiId(long apiId) {
        return this.playerRepository.findByApiId(apiId);
    }

    @Override
    public boolean isEmpty() {
        return this.playerRepository.count() == 0;
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
                dto.getPlayer().getId()
        );
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

//                            SeasonsByPlayerApiDto responseSeasonsByPlayerApiDto = getResponseSeasonsByPlayerApiDto(PLAYERS_BY_ID_AND_ALL_SEASONS, player.getApiId());
//                        int year = responseSeasonsByPlayerApiDto.getResponse().getLast();
//                        ResponsePlayerApiDto responsePlayer = this.getResponsePlayerApiDto(PLAYERS_BY_ID_AND_SEASON, player.getApiId(), year);
//                        StatisticsApiDto statistics = responsePlayer.getResponse().getFirst().getStatistics().getFirst();
//
//                        player.setTeam(statistics.getTeam().getName());
//                        player.setPosition(statistics.getGames().getPosition());
}
