package bg.softuni.footscore.service;

import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.model.entity.Team;

import java.util.List;
import java.util.Optional;

public interface LeagueTeamSeasonService {
    List<Team> getAllTeamsBySeasonIdAndLeagueId(long leagueId, long seasonId);

    Optional<Team> getTeamByLeagueIdAndSeasonIdAndTeamId(long leagueId, long seasonId, long teamId);

    List<Optional<LeagueTeamSeason>> getTeamsByLeagueIdAndSeasonId(long leagueId, long seasonId);

    Optional<LeagueTeamSeason> getByTeamIdAndLeagueId(long teamId, long leagueId);

    List<LeagueTeamSeason> getByTeamIdAndSeasonId(long teamId, long seasonId);

    void save(LeagueTeamSeason seasonLeagueTeam);
}
