package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;

import java.util.List;

public interface UserPlayerService {
    void createPlayer(UserPlayerDto userPlayerDto);

    List<UserPlayerDto> getAllPlayers();

    List<UserPlayerDto> getUserPlayers(Long id);
}
