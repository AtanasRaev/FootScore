package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.PlayerTeamSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerTeamSeasonRepository extends JpaRepository<PlayerTeamSeason, Long> {
    @Query("SELECT p FROM PlayerTeamSeason s JOIN s.player p WHERE s.season.id = :seasonId AND s.team.id = :teamId")
    List<Player> findPlayersByTeamIdAndSeasonId(long teamId, long seasonId);

    @Query("SELECT p FROM PlayerTeamSeason s JOIN s.player p WHERE s.season.id = :seasonId AND s.team.id = :teamId AND s.player.id = :playerId")
    Optional<Player> findPlayerByTeamIdAndSeasonId(long teamId, long seasonId, long playerId);

}
