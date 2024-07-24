package bg.softuni.footscore.service;

import bg.softuni.footscore.model.entity.SeasonLeagueTeam;
import bg.softuni.footscore.model.entity.Team;

import java.util.List;
import java.util.Optional;

public interface SeasonLeagueTeamService {
    List<Team> getAllTeamsBySeasonIdAndLeagueId(long leagueId, long seasonId);

    Optional<Team> getTeamByLeagueIdAndSeasonIdAndTeamId(long leagueId, long seasonId, long teamId);

    List<Optional<SeasonLeagueTeam>> getTeamByLeagueIdAndSeasonId(long leagueId, long seasonId);

    Optional<SeasonLeagueTeam> getByTeamIdAndLeagueId(long teamId, long leagueId);

    void save(SeasonLeagueTeam seasonLeagueTeam);
}
