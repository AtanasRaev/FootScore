package bg.softuni.footscore.userplayer.repository;


import bg.softuni.footscore.userplayer.model.entity.UserPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlayerRepository extends JpaRepository<UserPlayer, Long> {
    List<UserPlayer> findAllByUserId(Long userId);
}
