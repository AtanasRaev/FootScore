package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.entity.LeagueTeamSeason;

import java.util.List;

public interface LeagueTeamSeasonService {
    List<LeagueTeamSeasonPageDto> getAllByLeagueIdAndSeasonId(Long leagueId, Long seasonId);

    List<LeagueTeamSeasonPageDto> getByLeagueIdAndSeasonId(Long leagueId, Long seasonId);

    List<LeagueTeamSeasonPageDto> getByTeamIdAndLeagueId(Long teamId, Long leagueId);

    List<LeagueTeamSeasonPageDto> getByTeamIdAndSeasonId(Long teamId, Long seasonId);

    void save(LeagueTeamSeason seasonLeagueTeam);
}
