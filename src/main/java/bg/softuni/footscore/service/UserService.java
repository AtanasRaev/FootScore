package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.RegisterUserDto;

public interface UserService {
    void registerUser(RegisterUserDto registrationUserDto);
}
