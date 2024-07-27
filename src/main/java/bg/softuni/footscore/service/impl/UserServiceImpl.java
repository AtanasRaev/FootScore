package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.repository.UserEntityRepository;
import bg.softuni.footscore.service.RoleService;
import bg.softuni.footscore.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public void saveUser(UserEntity user) {
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void addFavoriteTeams(UserEntity user, List<Team> allByIds) {
        if (allByIds.isEmpty()) {
            return;
        }
        user.getFavoriteTeams().addAll(allByIds);
        this.userRepository.save(user);
    }
}
