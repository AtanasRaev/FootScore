package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.ResponsePlayerDetailsApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

import java.util.ArrayList;
import java.util.List;

public interface PlayerService {
    void saveApiPlayersForTeamAndSeason(TeamPageDto team, SeasonPageDto season);

    ResponsePlayerApiDto getResponsePlayerApiDto(String query, Long id, Integer seasonYear, Integer page);

    ResponsePlayerDetailsApiDto getResponsePlayerDetailsApiDto(String query, Long playerApiId);

    PlayerPageDto getPlayerByApiId(Long apiId);

    void fillMissingPlayerDetails(Long playerId);

    boolean isEmpty();

    PlayerPageDto getPlayerById(Long playerId);

    List<PlayerPageDto> getAllByIds(List<Long> playerIds);

    List<PlayerPageDto> getAllPlayers();

    List<PlayerPageDto> getAllSelectedPlayers(boolean bool);

    void setSelected(Long playerId, boolean selected);

    void setAllSelected(boolean selected);

    List<PlayerPageDto> getPlayersByPosition(String position, List<PlayerPageDto> validPlayers);

    List<PlayerPageDto> getAllSortedPlayers(String position, String search);

    List<PlayerPageDto> getAllPlayersByPosition(String position);

    List<PlayerTeamSeasonPageDto> getAllPlayers(long teamId, Long seasonId, List<PlayerTeamSeasonPageDto> allPlayers, TeamPageDto teamOptional);

    List<PlayerPageDto> getPlayersByPosition(String position, List<PlayerPageDto> validPlayers, List<PlayerTeamSeasonPageDto> allPlayers);
}
