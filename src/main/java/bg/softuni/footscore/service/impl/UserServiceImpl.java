package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.repository.UserEntityRepository;
import bg.softuni.footscore.service.RoleService;
import bg.softuni.footscore.service.UserService;
import bg.softuni.footscore.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserEntityRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(UserEntityRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public void registerUser(RegisterUserDto registrationUserDto) {
        UserEntity user = this.modelMapper.map(registrationUserDto, UserEntity.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        Role role;

        role = this.userRepository.count() == 0 ? this.roleService.getAdminRole() : this.roleService.getUserRole();

        if (role == null) {
            throw new IllegalStateException("Cannot find role");
        }

        user.setRole(role);
        this.userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void addFavoriteTeams(UserEntity user, List<Team> allByIds) {
        user.getFavoriteTeams().addAll(new HashSet<>(allByIds));
        this.userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> getUser() {
        return getUserByUsername(UserUtils.findUsername());
    }
}
