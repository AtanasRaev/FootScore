package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.PlayerStatisticsApiDto;
import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.StatisticsApiDto;
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

    public static final String PLAYERS_BY_TEAM_AND_SEASON = "%splayers?team=%d&season=%d";
    public static final String PLAYERS_BY_ID_AND_SEASON = "%splayers?id=%d&season=%d";

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
    public void saveApiPlayersForTeamAndSeason(long teamId, long seasonId) {
        Optional<Team> teamOptional = this.teamService.findById(teamId);

        teamOptional.ifPresent(team -> {
            Optional<Season> season = this.seasonService.getSeasonById(seasonId);

            if (season.isEmpty()) {
                throw new IllegalStateException("No season data found");
            }

            ResponsePlayerApiDto responseList = this.getResponse(PLAYERS_BY_TEAM_AND_SEASON, team.getApiId(), season.get().getYear());

            if (!responseList.getResponse().isEmpty()) {
                List<Player> playersToSave = new ArrayList<>();
                responseList.getResponse().forEach(dto -> {

                    if (this.getPlayerByApiId(dto.getPlayer().getId()).isEmpty()) {

                        Player player = createPlayer(dto);

                        ResponsePlayerApiDto responsePlayer = this.getResponse(PLAYERS_BY_ID_AND_SEASON, player.getApiId(), season.get().getYear());
                        StatisticsApiDto statistics = responsePlayer.getResponse().getFirst().getStatistics().getFirst();

                        player.setTeam(statistics.getTeam().getName());
                        player.setPosition(statistics.getGames().getPosition());

                        playersToSave.add(player);
                    }
                });
                this.playerRepository.saveAll(playersToSave);

                responseList.getResponse().forEach(dto -> {
                    Optional<Player> player = this.playerRepository.findByApiId(dto.getPlayer().getId());
                    if (player.isPresent()) {
                        Optional<Player> optionalPlayer = this.seasonTeamPlayerService.getPlayerByTeamIdAndSeasonId(teamId, seasonId, player.get().getId());
                        if (optionalPlayer.isEmpty()) {
                            SeasonTeamPlayer seasonTeamPlayer = new SeasonTeamPlayer();
                            seasonTeamPlayer.setSeason(season.get());
                            seasonTeamPlayer.setTeam(team);
                            seasonTeamPlayer.setPlayer(player.get());

                            this.seasonTeamPlayerService.save(seasonTeamPlayer);
                        }
                    }
                });
            }
        });
    }

    private static Player createPlayer(PlayerStatisticsApiDto dto) {
        String heightStr = dto.getPlayer().getHeight();
        String weightStr = dto.getPlayer().getWeight();

        Integer height = null;
        Integer weight = null;

        if (heightStr != null && !heightStr.trim().isEmpty()) {
            String[] heightParts = heightStr.split("\\s+");
            if (heightParts.length > 0) {
                height = Integer.parseInt(heightParts[0]);
            }
        }

        if (weightStr != null && !weightStr.trim().isEmpty()) {
            String[] weightParts = weightStr.split("\\s+");
            if (weightParts.length > 0) {
                weight = Integer.parseInt(weightParts[0]);
            }
        }

        return new Player(
                dto.getPlayer().getFirstname(),
                dto.getPlayer().getLastname(),
                dto.getPlayer().getAge(),
                LocalDate.parse(dto.getPlayer().getBirth().getDate()),
                dto.getPlayer().getNationality(),
                height,
                weight,
                dto.getPlayer().getPhoto(),
                dto.getPlayer().getId()
        );
    }


    @Override
    public ResponsePlayerApiDto getResponse(String query, long id, int seasonYear) {
        String url = String.format(query, this.apiConfig.getUrl(), id, seasonYear);

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(ResponsePlayerApiDto.class);
    }

    @Override
    public Optional<Player> getPlayerByApiId(long apiId) {
        return this.playerRepository.findByApiId(apiId);
    }

    @Override
    public boolean isEmpty() {
        return this.playerRepository.count() == 0;
    }
}
