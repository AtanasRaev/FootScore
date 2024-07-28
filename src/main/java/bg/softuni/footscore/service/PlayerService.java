package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.ResponsePlayerDetailsApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.Player;

import java.util.Optional;

public interface PlayerService {
    void saveApiPlayersForTeamAndSeason(TeamPageDto team, SeasonPageDto season);

    ResponsePlayerApiDto getResponsePlayerApiDto(String query, long id, int seasonYear, int page);

    ResponsePlayerDetailsApiDto getResponsePlayerDetailsApiDto(String query, long playerApiId);

    Optional<Player> getPlayerByApiId(long apiId);

    void fillMissingPlayerDetails(long playerId);

    boolean isEmpty();

    Optional<Player> getPlayerById(long playerId);
}
