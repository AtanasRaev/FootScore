package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    void saveApiTeamsForLeagueAndSeason(League league, Season season);

    ResponseTeamApiDto getResponse(long leagueApiId, int seasonYear);

    boolean isEmpty();

    List<Team> findAllByIds(List<Long> teams);

    Optional<Team> findById(long teamId);

    Optional<Team> getTeamByApiId(long apiId);

    Optional<Team> getTeamById(long teamId);

    void updateTeam(Team team);
}
