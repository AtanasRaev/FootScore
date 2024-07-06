package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaguesRepository extends JpaRepository<League, Long> {
}
