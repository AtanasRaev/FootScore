package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

import java.util.List;

public interface TeamService {
    void saveApiTeamsForLeagueAndSeason(LeaguePageDto league, SeasonPageDto season);

    ResponseTeamApiDto getResponse(Long leagueApiId, Integer seasonYear);

    boolean isEmpty();

    List<TeamPageDto> findAllByIds(List<Long> teams);

    TeamPageDto findById(Long teamId);

    TeamPageDto getTeamByApiId(Long apiId);

    TeamPageDto getTeamById(Long teamId);

    void updateTeam(TeamPageDto team);

    void fetchTeams(List<LeaguePageDto> leagues, List<SeasonPageDto> seasons);

    void saveAll(List<TeamPageDto> teamsToSave);
}
