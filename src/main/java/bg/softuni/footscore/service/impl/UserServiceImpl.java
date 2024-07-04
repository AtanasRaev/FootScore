package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.repository.UserEntityRepository;
import bg.softuni.footscore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserEntityRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserEntityRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterUserDto registrationUserDto) {
        UserEntity user = this.modelMapper.map(registrationUserDto, UserEntity.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        this.userRepository.save(user);
    }
}
