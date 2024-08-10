package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.teamDto.FormationDto;
import bg.softuni.footscore.model.dto.ResponseTeamStatisticsSeason;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.teamDto.*;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.TeamStatistics;
import bg.softuni.footscore.repository.TeamStatisticsRepository;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.TeamService;
import bg.softuni.footscore.service.TeamStatisticsService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class TeamStatisticsServiceImpl implements TeamStatisticsService {
    private final TeamStatisticsRepository teamStatisticsRepository;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final ModelMapper modelMapper;
    private final ApiConfig apiConfig;
    private final RestClient restClient;
    private final LeagueService leagueService;

    public TeamStatisticsServiceImpl(TeamStatisticsRepository teamStatisticsRepository,
                                     TeamService teamService,
                                     SeasonService seasonService,
                                     ModelMapper modelMapper,
                                     ApiConfig apiConfig,
                                     @Qualifier("genericRestClient")RestClient restClient,
                                     LeagueService leagueService) {
        this.teamStatisticsRepository = teamStatisticsRepository;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;

        this.leagueService = leagueService;
    }


    @Override
    @Transactional
    public void saveApiStatistics(Long leagueApiId, Long teamApiId, Integer seasonYear) {
        if (leagueApiId == null || teamApiId == null || seasonYear == null) {
            throw new IllegalArgumentException("League api id, team api id and season year must be provided");
        }

        Optional<TeamStatistics> optional = this.getByTeamApiIdAndSeasonYearAndLeagueApiId(teamApiId, seasonYear, leagueApiId);

        if (optional.isEmpty()) {
            ResponseTeamStatisticsSeason response = this.getResponse(leagueApiId, teamApiId, seasonYear);

            if (response.getResponse() != null) {
                TeamStatisticsDetailsApiDto dto = response.getResponse();
                TeamStatistics teamStatistic = this.modelMapper.map(dto, TeamStatistics.class);

                TeamPageDto optionalTeam = this.teamService.getTeamByApiId(dto.getTeam().getId());
                LeaguePageDto leagueByApiId = this.leagueService.getLeagueByApiId(dto.getLeague().getId());
                SeasonPageDto seasonByYear = this.seasonService.getSeasonByYear(seasonYear);

                if (optionalTeam != null && seasonByYear != null && leagueByApiId != null) {
                    teamStatistic.setSeason(this.modelMapper.map(seasonByYear, Season.class));
                    teamStatistic.setTeam(this.modelMapper.map(optionalTeam, Team.class));
                    teamStatistic.setLeague(this.modelMapper.map(leagueByApiId, League.class));
                    this.setGoalsStatistics(teamStatistic, dto);

                    for (LineupDto lineup : dto.getLineups()) {
                        teamStatistic.getLineups().put(lineup.getFormation(), lineup.getPlayed());
                    }

                    teamStatistic.setCleanSheetsTotal(dto.getCleanSheet().getTotal());

                    if (!optionalTeam.getStatistics().contains(this.modelMapper.map(teamStatistic, TeamStatisticsPageDto.class))) {
                        this.teamStatisticsRepository.save(teamStatistic);

                        optionalTeam.getStatistics().add(this.modelMapper.map(teamStatistic, TeamStatisticsPageDto.class));
                        this.teamService.updateTeam(optionalTeam);
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

    @Override
    public List<FormationDto> getAllFormations() {
        return this.teamStatisticsRepository
                .findAll()
                .stream()
                .flatMap(teamStatistics -> teamStatistics.getLineups().keySet().stream())
                .map(FormationDto::new)
                .toList();
    }

    public void setGoalsStatistics(TeamStatistics teamStatistic, TeamStatisticsDetailsApiDto dto) {
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
