package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.SeasonsByPlayerApiDto;
import bg.softuni.footscore.model.entity.Player;

import java.util.Optional;

public interface PlayerService {
    void saveApiPlayersForTeamAndSeason(long teamId, long seasonId);

    ResponsePlayerApiDto getResponsePlayerApiDto(String query, long id, int seasonYear);

    SeasonsByPlayerApiDto getResponseSeasonsByPlayerApiDto(String query, long id);

    Optional<Player> getPlayerByApiId(long apiId);

    boolean isEmpty();
}
