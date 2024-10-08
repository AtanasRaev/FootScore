package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.teamDto.FormationDto;
import bg.softuni.footscore.model.entity.TeamStatistics;

import java.util.List;
import java.util.Optional;

public interface TeamStatisticsService {
    void saveApiStatistics(Long leagueApiId, Long teamApiId, Integer seasonYear);

    Optional<TeamStatistics> getByTeamIdAndSeasonYearAndLeagueId(long teamId, int seasonYear, long leagueId);

    List<FormationDto> getAllFormations();
}