package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueTeamSeasonRepository extends JpaRepository<LeagueTeamSeason, Long> {

    List<LeagueTeamSeason> findByTeamIdAndLeagueId(long teamId, long leagueId);

    List<LeagueTeamSeason> findByLeagueIdAndSeasonId(long leagueId, long seasonId);

    List<LeagueTeamSeason> findByTeamIdAndSeasonId(long teamId, long seasonId);
}
