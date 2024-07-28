package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.ResponsePlayerDetailsApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

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
}
