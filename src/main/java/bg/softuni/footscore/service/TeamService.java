package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.TeamPageDto;
import bg.softuni.footscore.model.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    void saveApiTeamsForLeagueAndSeason(long leagueId, long seasonId);

    ResponseTeamApiDto getResponse(long leagueApiId, int seasonYear);

    boolean isEmpty();

    List<TeamPageDto> findAllByIds(List<Long> teams);

    Optional<Team> findById(long teamId);

    Optional<Team> getTeamByApiId(long apiId);

    Optional<Team> getTeamById(long teamId);
}
