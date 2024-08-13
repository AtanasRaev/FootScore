package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.DreamTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DreamTeamRepository extends JpaRepository<DreamTeam, Long> {
    List<DreamTeam> findAllByUserId(Long userId);
}
