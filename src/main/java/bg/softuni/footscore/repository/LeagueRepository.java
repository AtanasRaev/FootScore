package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    List<League> findByCountryNameAndSelectedNot(String name, boolean selected);
}
