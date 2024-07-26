package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseTeamStatisticsSeason;
import bg.softuni.footscore.model.entity.TeamStatistics;

import java.util.Optional;

public interface TeamStatisticsService {
    void saveApiStatistics(long leagueApiId, long teamApiId, int seasonYear);

    ResponseTeamStatisticsSeason getResponse(long leagueApiId, long teamApiId, int seasonYear);

    Optional<TeamStatistics> getByTeamApiIdAndSeasonYearAndLeagueApiId(long teamApiId, int seasonYear, long leagueApiId);
}
