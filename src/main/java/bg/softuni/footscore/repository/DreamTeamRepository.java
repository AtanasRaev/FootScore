package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.DreamTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DreamTeamRepository extends JpaRepository<DreamTeam, Long> {
}
