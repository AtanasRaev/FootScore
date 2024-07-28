package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.FormationDto;
import bg.softuni.footscore.model.dto.ResponseTeamStatisticsSeason;
import bg.softuni.footscore.model.dto.teamDto.TeamStatisticsPageDto;
import bg.softuni.footscore.model.entity.TeamStatistics;

import java.util.List;
import java.util.Optional;

public interface TeamStatisticsService {
    void saveApiStatistics(Long leagueApiId, Long teamApiId, Integer seasonYear);

    ResponseTeamStatisticsSeason getResponse(long leagueApiId, long teamApiId, int seasonYear);

    Optional<TeamStatistics> getByTeamApiIdAndSeasonYearAndLeagueApiId(long teamApiId, int seasonYear, long leagueApiId);

    Optional<TeamStatistics> getByTeamIdAndSeasonYearAndLeagueId(long teamId, int seasonYear, long leagueId);

    List<FormationDto> getAllFormations();
}
