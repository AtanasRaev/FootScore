package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.SeasonLeagueTeam;
import bg.softuni.footscore.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonLeagueTeamRepository extends JpaRepository<SeasonLeagueTeam, Long> {

    @Query("SELECT t FROM SeasonLeagueTeam s JOIN s.team t WHERE s.season.id = :seasonId AND s.league.id = :leagueId")
    List<Team> findTeamsByLeagueIdAndSeasonId(Long leagueId, Long seasonId);

    @Query("SELECT t FROM SeasonLeagueTeam s JOIN s.team t WHERE s.season.id = :seasonId AND s.league.id = :leagueId AND s.team.id = :teamId")
    Optional<Team> findTeamByLeagueIdAndSeasonIdAndTeamId(Long leagueId, Long seasonId, Long teamId);

    Optional<SeasonLeagueTeam> findByTeamIdAndLeagueId(long teamId, long leagueId);

    @Query("FROM SeasonLeagueTeam s WHERE s.season.id = :seasonId AND s.league.id = :leagueId")
    List<Optional<SeasonLeagueTeam>> findTeamByLeagueIdAndSeasonId(long leagueId, long seasonId);

    List<SeasonLeagueTeam> findByTeamIdAndSeasonId(long teamId, long seasonId);
}
