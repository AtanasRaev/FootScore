package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.TeamStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamStatisticsRepository extends JpaRepository<TeamStatistics, Long> {

    @Query("FROM TeamStatistics ts WHERE ts.team.apiId = :teamApiId AND ts.league.apiId =:leagueApiId AND ts.season.year = :seasonYear")
    Optional<TeamStatistics> findByTeamApiIdAndSeasonYearAndLeagueApiId(Long teamApiId, Integer seasonYear, Long leagueApiId);

    Optional<TeamStatistics> findByTeamIdAndSeasonYearAndLeagueId(long teamId, int seasonYear, long leagueId);
}
