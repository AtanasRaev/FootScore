package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

import java.util.List;

public interface UserService {
    void registerUser(RegisterUserDto registrationUserDto);

    UserEntityPageDto getUserByUsername(String username);

    void updateUser(UserEntityPageDto userEntityPageDto);

    void addFavoriteTeams(UserEntityPageDto user, List<TeamPageDto> allByIds);

    UserEntityPageDto getUser();
}
