package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.repository.UserEntityRepository;
import bg.softuni.footscore.service.RoleService;
import bg.softuni.footscore.service.UserService;
import bg.softuni.footscore.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public UserEntityPageDto getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(u -> this.modelMapper.map(u, UserEntityPageDto.class))
                .orElse(null);
    }

    public void updateAddFavoriteTeams(UserEntityPageDto userEntityPageDto) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(userEntityPageDto.getUsername());

        Set<TeamPageDto> favoriteTeams = userEntityPageDto.getFavoriteTeams();
        Set<Team> collect = favoriteTeams.stream().map(t -> this.modelMapper.map(t, Team.class)).collect(Collectors.toSet());

        byUsername.ifPresent(user -> {
            user.getFavoriteTeams().addAll(collect);
            this.userRepository.save(user);
        });

    }

    public void updateAddFavoritePlayers(UserEntityPageDto userEntityPageDto) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(userEntityPageDto.getUsername());

        Set<PlayerPageDto> favoritePlayers = userEntityPageDto.getFavoritePlayers();
        Set<Player> collect = favoritePlayers.stream().map(p -> this.modelMapper.map(p, Player.class)).collect(Collectors.toSet());

        byUsername.ifPresent(user -> {
            user.getFavoritePlayers().addAll(collect);
            this.userRepository.save(user);
        });
    }

    @Override
    public void addFavoriteTeams(UserEntityPageDto dto, List<TeamPageDto> allByIds) {
        dto.getFavoriteTeams().addAll(new HashSet<>(allByIds));
        this.updateAddFavoriteTeams(dto);
    }

    @Override
    public UserEntityPageDto getUser() {
        return getUserByUsername(UserUtils.findUsername());
    }

    @Override
    public void addFavoritePlayers(UserEntityPageDto dto, List<PlayerPageDto> allByIds) {
        dto.getFavoritePlayers().addAll(new HashSet<>(allByIds));
        this.updateAddFavoritePlayers(dto);
    }

    @Override
    public void removeFavoriteTeams(UserEntityPageDto user, List<TeamPageDto> allByIds) {
        for (TeamPageDto allById : allByIds) {
            user.getFavoriteTeams().removeIf(favoriteTeam -> allById.getId() == favoriteTeam.getId());
        }
        this.removeTeamsAndSaveUser(user);
    }

    private void removeTeamsAndSaveUser(UserEntityPageDto user) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(user.getUsername());

        Set<TeamPageDto> dtoFavoriteTeams = new HashSet<>(user.getFavoriteTeams());
        Set<Team> collect = dtoFavoriteTeams.stream().map(dto -> this.modelMapper.map(dto, Team.class)).collect(Collectors.toSet());

        byUsername.ifPresent(userEntity -> {
            userEntity.setFavoriteTeams(collect);
            this.userRepository.save(userEntity);
        });
    }

    @Override
    public void removeFavoritePlayers(UserEntityPageDto user, List<PlayerPageDto> allByIds) {
        for (PlayerPageDto allById : allByIds) {
            user.getFavoritePlayers().removeIf(favoritePlayer -> allById.getId() == favoritePlayer.getId());
        }
        this.removePlayersAndSaveUser(user);
    }

    private void removePlayersAndSaveUser(UserEntityPageDto user) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(user.getUsername());

        Set<PlayerPageDto> dtoFavoritePlayers = new HashSet<>(user.getFavoritePlayers());
        Set<Player> collect = dtoFavoritePlayers.stream().map(dto -> this.modelMapper.map(dto, Player.class)).collect(Collectors.toSet());

        byUsername.ifPresent(userEntity -> {
                userEntity.setFavoritePlayers(collect);
                this.userRepository.save(userEntity);
        });
    }
}
