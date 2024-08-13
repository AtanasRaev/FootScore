package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueTeamSeasonRepository extends JpaRepository<LeagueTeamSeason, Long> {

    List<LeagueTeamSeason> findByLeagueIdAndSeasonId(long leagueId, long seasonId);

    List<LeagueTeamSeason> findByTeamIdAndSeasonId(long teamId, long seasonId);
}
