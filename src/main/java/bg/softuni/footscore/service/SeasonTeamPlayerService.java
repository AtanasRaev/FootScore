package bg.softuni.footscore.service;

import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.SeasonTeamPlayer;

import java.util.List;
import java.util.Optional;

public interface SeasonTeamPlayerService {
    List<Player> getAllPlayersBySeasonIdAndTeamId(long teamId, long seasonId);

    Optional<Player> getPlayerByTeamIdAndSeasonId(long teamId, long seasonId, long playerId);

    void save(SeasonTeamPlayer seasonTeamPlayer);

}
