package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ResponseTeamStatisticsSeason;
import bg.softuni.footscore.model.dto.teamDto.*;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.TeamStatistics;
import bg.softuni.footscore.repository.TeamStatisticsRepository;
import bg.softuni.footscore.service.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class TeamStatisticsServiceImpl implements TeamStatisticsService {
    private final TeamStatisticsRepository teamStatisticsRepository;
    private final LeagueTeamSeasonService leagueTeamSeasonService;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final ModelMapper modelMapper;
    private final ApiConfig apiConfig;
    private final RestClient restClient;
    private final LeagueService leagueService;

    public TeamStatisticsServiceImpl(TeamStatisticsRepository teamStatisticsRepository,
                                     LeagueTeamSeasonService leagueTeamSeasonService,
                                     TeamService teamService,
                                     SeasonService seasonService,
                                     ModelMapper modelMapper,
                                     ApiConfig apiConfig,
                                     RestClient restClient,
                                     LeagueService leagueService) {
        this.teamStatisticsRepository = teamStatisticsRepository;
        this.leagueTeamSeasonService = leagueTeamSeasonService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;

        this.leagueService = leagueService;
    }


    @Override
    @Transactional
    public void saveApiStatistics(long leagueApiId, long teamApiId, int seasonYear) {
        Optional<TeamStatistics> optional = this.getByTeamApiIdAndSeasonYearAndLeagueApiId(teamApiId, seasonYear, leagueApiId);

        if (optional.isEmpty()) {
            ResponseTeamStatisticsSeason response = this.getResponse(leagueApiId, teamApiId, seasonYear);

            if (response.getResponse() != null) {
                TeamStatisticsDetailsDto dto = response.getResponse();
                TeamStatistics teamStatistic = this.modelMapper.map(dto, TeamStatistics.class);

                Optional<Team> optionalTeam = this.teamService.getTeamByApiId(dto.getTeam().getId());
                Optional<League> optionalLeague = this.leagueService.getLeagueByApiId(dto.getLeague().getId());
                Optional<Season> optionalSeason = this.seasonService.getSeasonByYear(seasonYear);

                if (optionalTeam.isPresent() && optionalSeason.isPresent() && optionalLeague.isPresent()) {
                    teamStatistic.setSeason(optionalSeason.get());
                    teamStatistic.setTeam(optionalTeam.get());
                    teamStatistic.setLeague(optionalLeague.get());
                    this.setGoalsStatistics(teamStatistic, dto);

                    for (LineupDto lineup : dto.getLineups()) {
                        teamStatistic.getLineups().put(lineup.getFormation(), lineup.getPlayed());
                    }

                    teamStatistic.setCleanSheetsTotal(dto.getCleanSheet().getTotal());

                    if (!optionalTeam.get().getStatistics().contains(teamStatistic)) {
                        this.teamStatisticsRepository.save(teamStatistic);

                        optionalTeam.get().getStatistics().add(teamStatistic);
                        this.teamService.updateTeam(optionalTeam.get());
                    }
                }
            }
        }
    }


    @Override
    public ResponseTeamStatisticsSeason getResponse(long leagueApiId, long teamApiId, int seasonYear) {
        String url = this.apiConfig.getUrl() + "teams/statistics?league=" + leagueApiId + "&season=" + seasonYear + "&team=" + teamApiId;

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(ResponseTeamStatisticsSeason.class);
    }

    @Override
    @Transactional
    public Optional<TeamStatistics> getByTeamApiIdAndSeasonYearAndLeagueApiId(long teamApiId, int seasonYear, long leagueApiId) {
        return this.teamStatisticsRepository.findByTeamApiIdAndSeasonYearAndLeagueApiId(teamApiId, seasonYear, leagueApiId);
    }

    @Override
    public Optional<TeamStatistics> getByTeamIdAndSeasonYearAndLeagueId(long teamId, int seasonYear, long leagueId) {
        return this.teamStatisticsRepository.findByTeamIdAndSeasonYearAndLeagueId(teamId, seasonYear, leagueId);
    }

    public void setGoalsStatistics(TeamStatistics teamStatistic, TeamStatisticsDetailsDto dto) {
        GoalsDto goals = dto.getGoals();
        GoalsDetailDto forGoals = goals.getForGoals();
        TotalDto forTotal = forGoals.getTotal();
        TotalDto againstTotal = goals.getAgainst().getTotal();

        teamStatistic.setGoalsForTotal(forTotal.getTotal());
        teamStatistic.setGoalsForAway(forTotal.getAway());
        teamStatistic.setGoalsForHome(forTotal.getHome());
        teamStatistic.setGoalsAgainstTotal(againstTotal.getTotal());
        teamStatistic.setGoalDifference(teamStatistic.getGoalsForTotal() - teamStatistic.getGoalsAgainstTotal());
    }

}
