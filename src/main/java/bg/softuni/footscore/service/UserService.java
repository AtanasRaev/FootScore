package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.model.dto.UserEditDto;
import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    void registerUser(RegisterUserDto registrationUserDto);

    UserEntityPageDto getUserByUsername(String username);

    void addFavoriteTeams(UserEntityPageDto user, List<TeamPageDto> allByIds);

    void addFavoritePlayers(UserEntityPageDto user, List<PlayerPageDto> allByIds);

    void removeFavoriteTeams(UserEntityPageDto user, List<TeamPageDto> allByIds);

    void removeFavoritePlayers(UserEntityPageDto user, List<PlayerPageDto> allByIds);

    void updateUsername(UserEditDto dto);

    UserEntityPageDto getUser();

    boolean isUniqueEmail(String value);

    boolean isUniqueUsername(String value);
}
