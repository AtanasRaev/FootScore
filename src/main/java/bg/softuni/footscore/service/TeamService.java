package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    void saveApiTeamsForLeagueAndSeason(LeaguePageDto league, SeasonPageDto season);

    ResponseTeamApiDto getResponse(long leagueApiId, int seasonYear);

    boolean isEmpty();

    List<Team> findAllByIds(List<Long> teams);

    Optional<Team> findById(long teamId);

    Optional<Team> getTeamByApiId(long apiId);

    Optional<Team> getTeamById(long teamId);

    void updateTeam(Team team);

    void fetchTeams(List<LeaguePageDto> leagues, List<SeasonPageDto> seasons);
}
