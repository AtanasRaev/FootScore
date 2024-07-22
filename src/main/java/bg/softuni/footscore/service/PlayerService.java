package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.entity.Player;

import java.util.Optional;

public interface PlayerService {
    void saveApiPlayersForTeamAndSeason(long teamId, long seasonId);

    ResponsePlayerApiDto getResponse(String query, long teamApiId, int seasonYear);

    Optional<Player> getPlayerByApiId(long apiId);

    boolean isEmpty();
}
