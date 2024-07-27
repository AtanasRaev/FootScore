package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(RegisterUserDto registrationUserDto);

    Optional<UserEntity> getUserByUsername(String username);

    void saveUser(UserEntity user);

    void addFavoriteTeams(UserEntity user, List<Team> allByIds);
}
