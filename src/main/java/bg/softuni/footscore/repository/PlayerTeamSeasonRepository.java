package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.PlayerTeamSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerTeamSeasonRepository extends JpaRepository<PlayerTeamSeason, Long> {

    List<PlayerTeamSeason> findByTeamIdAndSeasonId(Long teamId, Long seasonId);

}
