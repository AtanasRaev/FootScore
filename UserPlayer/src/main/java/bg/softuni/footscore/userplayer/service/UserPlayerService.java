package bg.softuni.footscore.userplayer.service;

import bg.softuni.footscore.userplayer.model.dto.CreateUserPlayerDto;
import bg.softuni.footscore.userplayer.model.dto.UserPlayerDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserPlayerService {
    void addUserPlayer(CreateUserPlayerDto dto);

    List<UserPlayerDto> getAllUserPlayers();

    List<UserPlayerDto> getAllUserPlayersById(Long id);

    void deleteUserPlayerById(Long id);

   UserPlayerDto getUserPlayerById(Long id);

    UserPlayerDto updateUserPlayerById(Long id, UserPlayerDto dto);
}
