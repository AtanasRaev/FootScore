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

    @Query("SELECT t FROM LeagueTeamSeason s JOIN s.team t WHERE s.season.id = :seasonId AND s.league.id = :leagueId")
    List<Team> findTeamsByLeagueIdAndSeasonId(Long leagueId, Long seasonId);

    @Query("SELECT t FROM LeagueTeamSeason s JOIN s.team t WHERE s.season.id = :seasonId AND s.league.id = :leagueId AND s.team.id = :teamId")
    Optional<Team> findTeamByLeagueIdAndSeasonIdAndTeamId(Long leagueId, Long seasonId, Long teamId);

    Optional<LeagueTeamSeason> findByTeamIdAndLeagueId(long teamId, long leagueId);

    @Query("FROM LeagueTeamSeason s WHERE s.season.id = :seasonId AND s.league.id = :leagueId")
    List<Optional<LeagueTeamSeason>> findTeamByLeagueIdAndSeasonId(long leagueId, long seasonId);

    List<LeagueTeamSeason> findByTeamIdAndSeasonId(long teamId, long seasonId);
}
