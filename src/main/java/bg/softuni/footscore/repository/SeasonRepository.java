package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Season findByYear(int year);
}