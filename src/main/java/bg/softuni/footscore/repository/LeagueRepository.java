package bg.softuni.footscore.repository;

import bg.softuni.footscore.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    List<League> findBySelected(boolean selected);

    List<League> findByCountryName(String name);

    @Modifying
    @Query("UPDATE League l SET l.selected = :selected WHERE l.name = :name")
    void updateLeagueBySelectedStatusByName(@Param("name") String name, @Param("selected") boolean selected);
}
