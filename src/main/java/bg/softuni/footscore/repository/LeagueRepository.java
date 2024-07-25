package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    List<League> findByCountryNameAndSelectedNot(String name, boolean selected);

    List<League> findBySelectedTrue();

    Optional<League> findByApiId(long apiId);
}
