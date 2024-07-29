package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByApiId(long apiId);

    List<Player> findByIsSelected(boolean isSelected);
}
