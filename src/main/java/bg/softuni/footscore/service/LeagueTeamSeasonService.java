package bg.softuni.footscore.service;

import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.model.entity.Team;

import java.util.List;
import java.util.Optional;

public interface LeagueTeamSeasonService {
    List<LeagueTeamSeason> getAllByLeagueIdAndSeasonId(long leagueId, long seasonId);;

    List<LeagueTeamSeason> getByLeagueIdAndSeasonId(long leagueId, long seasonId);

    List<LeagueTeamSeason> getByTeamIdAndLeagueId(long teamId, long leagueId);

    List<LeagueTeamSeason> getByTeamIdAndSeasonId(long teamId, long seasonId);

    void save(LeagueTeamSeason seasonLeagueTeam);
}
