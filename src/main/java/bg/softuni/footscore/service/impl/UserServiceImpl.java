package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.userDto.RegisterUserDto;
import bg.softuni.footscore.model.dto.userDto.UserEditDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.repository.UserEntityRepository;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.RoleService;
import bg.softuni.footscore.service.TeamService;
import bg.softuni.footscore.service.UserService;
import bg.softuni.footscore.utils.RequestContextHolder;
import bg.softuni.footscore.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final UserEntityRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RequestContextHolder requestContextHolder;


    public UserServiceImpl(PlayerService playerService,
                           TeamService teamService,
                           UserEntityRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder,
                           RoleService roleService,
                           RequestContextHolder requestContextHolder) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.requestContextHolder = requestContextHolder;
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

    @Override
    public void addTeamsToFavorites(List<Long> teamIds, UserEntityPageDto user) {
        List<TeamPageDto> allByIds  = new ArrayList<>();
        if (teamIds != null && !teamIds.isEmpty()) {
            allByIds = this.teamService.findAllByIds(teamIds);
        }

        if (!user.getFavoriteTeams().containsAll(allByIds)) {
            addFavoriteTeams(user, allByIds);
        }
    }

    @Override
    public UserEntityPageDto getUser() {
        return getUserByUsername(UserUtils.findUsername());
    }

    @Override
    public boolean isUniqueUsername(String username) {
        return this.userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public List<PlayerPageDto> getFavoritePlayers(List<PlayerPageDto> players, UserEntityPageDto user) {
        List<PlayerPageDto> list = new ArrayList<>();
        for (PlayerPageDto player : players) {
            for (PlayerPageDto favorite : user.getFavoritePlayers().stream().toList()) {
                if (player.getId() == favorite.getId()) {
                    list.add(player);
                }
            }
        }
        return list;
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return this.userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public void addPlayersToFavorites(List<Long> playerIds, UserEntityPageDto user) {
        if (user != null) {
            List<PlayerPageDto> allByIds = new ArrayList<>();
            if (playerIds != null && !playerIds.isEmpty()) {
                allByIds = this.playerService.getAllByIds(playerIds);
            }

            if (!user.getFavoritePlayers().containsAll(allByIds)) {
                addFavoritePlayers(user, allByIds);
            }
        }
    }

    @Override
    public void removeFavoriteTeams(UserEntityPageDto user, List<TeamPageDto> allByIds) {
        for (TeamPageDto allById : allByIds) {
            user.getFavoriteTeams().removeIf(favoriteTeam -> allById.getId() == favoriteTeam.getId());
        }
        this.removeTeamsAndSaveUser(user);
    }

    @Override
    public void removeFavoritePlayers(UserEntityPageDto user, List<PlayerPageDto> allByIds) {
        for (PlayerPageDto allById : allByIds) {
            user.getFavoritePlayers().removeIf(favoritePlayer -> allById.getId() == favoritePlayer.getId());
        }
        this.removePlayersAndSaveUser(user);
    }

    @Override
    public List<TeamPageDto> getFavoriteTeams(List<TeamPageDto> teams, UserEntityPageDto user) {
        List<TeamPageDto> list = new ArrayList<>();
        for (TeamPageDto team : teams) {
            for (TeamPageDto favorite : user.getFavoriteTeams().stream().toList()) {
                if (team.getId() == favorite.getId()) {
                    list.add(team);
                }
            }

        }
        return list;
    }

    @Override
    public void updateUsername(UserEditDto dto, String currentUsername) {
        if (this.userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        Optional<UserEntity> user = this.userRepository.findByUsername(currentUsername);

        user.ifPresent(u -> {
            u.setUsername(dto.getUsername());
            this.userRepository.save(u);
        });

        UserUtils.logoutUser(requestContextHolder.getRequest(), requestContextHolder.getResponse());
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

    private void removePlayersAndSaveUser(UserEntityPageDto user) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(user.getUsername());

        Set<PlayerPageDto> dtoFavoritePlayers = new HashSet<>(user.getFavoritePlayers());
        Set<Player> collect = dtoFavoritePlayers.stream().map(dto -> this.modelMapper.map(dto, Player.class)).collect(Collectors.toSet());

        byUsername.ifPresent(userEntity -> {
            userEntity.setFavoritePlayers(collect);
            this.userRepository.save(userEntity);
        });
    }

    private void updateAddFavoriteTeams(UserEntityPageDto userEntityPageDto) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(userEntityPageDto.getUsername());

        Set<TeamPageDto> favoriteTeams = userEntityPageDto.getFavoriteTeams();
        Set<Team> collect = favoriteTeams.stream().map(t -> this.modelMapper.map(t, Team.class)).collect(Collectors.toSet());

        byUsername.ifPresent(user -> {
            user.getFavoriteTeams().addAll(collect);
            this.userRepository.save(user);
        });

    }

    private void addFavoriteTeams(UserEntityPageDto dto, List<TeamPageDto> allByIds) {
        dto.getFavoriteTeams().addAll(new HashSet<>(allByIds));
        this.updateAddFavoriteTeams(dto);
    }

    private void updateAddFavoritePlayers(UserEntityPageDto userEntityPageDto) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(userEntityPageDto.getUsername());

        Set<PlayerPageDto> favoritePlayers = userEntityPageDto.getFavoritePlayers();
        Set<Player> collect = favoritePlayers.stream().map(p -> this.modelMapper.map(p, Player.class)).collect(Collectors.toSet());

        byUsername.ifPresent(user -> {
            user.getFavoritePlayers().addAll(collect);
            this.userRepository.save(user);
        });
    }

    private void addFavoritePlayers(UserEntityPageDto dto, List<PlayerPageDto> allByIds) {
        dto.getFavoritePlayers().addAll(new HashSet<>(allByIds));
        this.updateAddFavoritePlayers(dto);
    }
}
