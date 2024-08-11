package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.userDto.RegisterUserDto;
import bg.softuni.footscore.model.dto.userDto.UserEditDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    void registerUser(RegisterUserDto registrationUserDto);

    UserEntityPageDto getUserByUsername(String username);

    void addTeamsToFavorites(List<Long> teamIds, UserEntityPageDto user);

    void addPlayersToFavorites(List<Long> playerIds, UserEntityPageDto user);

    void removeFavoriteTeams(UserEntityPageDto user, List<TeamPageDto> allByIds);

    void removeFavoritePlayers(UserEntityPageDto user, List<PlayerPageDto> allByIds);

    List<TeamPageDto> getFavoriteTeams(List<TeamPageDto> teams, UserEntityPageDto user);

    void updateUsername(UserEditDto dto);

    UserEntityPageDto getUser();

    boolean isUniqueEmail(String value);

    boolean isUniqueUsername(String value);

    List<PlayerPageDto> getFavoritePlayers(List<PlayerPageDto> players, UserEntityPageDto user);
}
