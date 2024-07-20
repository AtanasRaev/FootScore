package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);

    Team findByApiId(long id);

}
