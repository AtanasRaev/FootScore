package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.dto.userDto.RegisterUserDto;
import bg.softuni.footscore.model.dto.userDto.UserEditDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.repository.UserEntityRepository;
import bg.softuni.footscore.service.impl.UserServiceImpl;
import bg.softuni.footscore.utils.RequestContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Mock
    private RequestContextHolder requestContextHolder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addTeamsToFavorites_shouldNotAddDuplicateTeams() {
        UserEntityPageDto user = new UserEntityPageDto();
        TeamPageDto existingTeam = new TeamPageDto();
        existingTeam.setId(1L);
        user.setFavoriteTeams(new HashSet<>(Collections.singletonList(existingTeam)));

        TeamPageDto newTeam = new TeamPageDto();
        newTeam.setId(1L);
        List<TeamPageDto> teamsToAdd = Collections.singletonList(newTeam);

        userService.addTeamsToFavorites(Collections.singletonList(1L), user);

        assertEquals(1, user.getFavoriteTeams().size());
        assertTrue(user.getFavoriteTeams().contains(existingTeam));
    }

    @Test
    public void removeFavoriteTeams_shouldNotRemoveNonexistentTeams() {
        UserEntityPageDto user = new UserEntityPageDto();
        TeamPageDto existingTeam = new TeamPageDto();
        existingTeam.setId(1L);
        user.setFavoriteTeams(new HashSet<>(Collections.singletonList(existingTeam)));

        TeamPageDto nonExistentTeam = new TeamPageDto();
        nonExistentTeam.setId(2L);
        List<TeamPageDto> teamsToRemove = Collections.singletonList(nonExistentTeam);

        userService.removeFavoriteTeams(user, teamsToRemove);

        assertEquals(1, user.getFavoriteTeams().size());
        assertTrue(user.getFavoriteTeams().contains(existingTeam));
    }

    @Test
    public void addPlayersToFavorites_shouldNotAddDuplicatePlayers() {
        UserEntityPageDto user = new UserEntityPageDto();
        PlayerPageDto existingPlayer = new PlayerPageDto();
        existingPlayer.setId(1L);
        user.setFavoritePlayers(new HashSet<>(Collections.singletonList(existingPlayer)));

        PlayerPageDto newPlayer = new PlayerPageDto();
        newPlayer.setId(1L);  // Same ID as the existing player
        List<PlayerPageDto> playersToAdd = Collections.singletonList(newPlayer);

        userService.addPlayersToFavorites(Collections.singletonList(1L), user);

        assertEquals(1, user.getFavoritePlayers().size());
        assertTrue(user.getFavoritePlayers().contains(existingPlayer));
    }

    @Test
    public void removeFavoritePlayers_shouldNotRemoveNonexistentPlayers() {
        UserEntityPageDto user = new UserEntityPageDto();
        PlayerPageDto existingPlayer = new PlayerPageDto();
        existingPlayer.setId(1L);
        user.setFavoritePlayers(new HashSet<>(Collections.singletonList(existingPlayer)));

        PlayerPageDto nonExistentPlayer = new PlayerPageDto();
        nonExistentPlayer.setId(2L);
        List<PlayerPageDto> playersToRemove = Collections.singletonList(nonExistentPlayer);

        userService.removeFavoritePlayers(user, playersToRemove);

        assertEquals(1, user.getFavoritePlayers().size());
        assertTrue(user.getFavoritePlayers().contains(existingPlayer));
    }

    @Test
    public void updateUsername_shouldThrowException_whenNewUsernameIsNotUnique() {
        UserEditDto dto = new UserEditDto();
        dto.setUsername("existingUsername");

        when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new UserEntity()));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            userService.updateUsername(dto, "currentUsername");
        });

        assertEquals("Username already exists", thrown.getMessage());
    }

    @Test
    public void updateUsername_shouldHandleLogoutAfterSuccessfulUpdate() {
        UserEditDto dto = new UserEditDto();
        dto.setUsername("newUsername");

        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setUsername("oldUsername");

        when(userRepository.findByUsername("oldUsername")).thenReturn(Optional.of(existingUserEntity));
        when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());

        userService.updateUsername(dto, "oldUsername");

        assertEquals("newUsername", existingUserEntity.getUsername());
        verify(userRepository).save(existingUserEntity);
        verify(requestContextHolder).getRequest();
        verify(requestContextHolder).getResponse();
    }

    @Test
    public void registerUser_shouldSaveUserWithAdminRole_whenNoUsersInRepository() {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setEmail("test@example.com");

        UserEntity userEntity = new UserEntity();
        Role adminRole = new Role();

        when(modelMapper.map(dto, UserEntity.class)).thenReturn(userEntity);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(roleService.getAdminRole()).thenReturn(adminRole);
        when(userRepository.count()).thenReturn(0L);

        userService.registerUser(dto);

        verify(userRepository).save(userEntity);
    }

    @Test
    public void registerUser_shouldThrowException_whenRoleIsNull() {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setEmail("test@example.com");

        UserEntity userEntity = new UserEntity();
        when(modelMapper.map(dto, UserEntity.class)).thenReturn(userEntity);

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        when(roleService.getAdminRole()).thenReturn(null);
        when(userRepository.count()).thenReturn(0L);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("Cannot find role", thrown.getMessage());
    }

    @Test
    public void registerUser_shouldSaveUserWithUserRole_whenUsersInRepository() {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setUsername("testUser");
        dto.setPassword("password");
        dto.setEmail("test@example.com");

        UserEntity userEntity = new UserEntity();
        Role userRole = new Role();

        when(modelMapper.map(dto, UserEntity.class)).thenReturn(userEntity);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(roleService.getUserRole()).thenReturn(userRole);
        when(userRepository.count()).thenReturn(1L);

        userService.registerUser(dto);

        verify(userRepository).save(userEntity);
    }

    @Test
    public void getUserByUsername_shouldReturnUserEntityPageDto_whenUserExists() {
        UserEntity userEntity = new UserEntity();
        UserEntityPageDto dto = new UserEntityPageDto();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserEntityPageDto.class)).thenReturn(dto);

        UserEntityPageDto result = userService.getUserByUsername("testuser");

        assertNotNull(result);
    }

    @Test
    public void getUserByUsername_shouldReturnNull_whenUserDoesNotExist() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        UserEntityPageDto result = userService.getUserByUsername("testuser");

        assertNull(result);
    }

    @Test
    public void addTeamsToFavorites_shouldAddTeams_whenNotAlreadyFavorite() {
        UserEntityPageDto user = new UserEntityPageDto();
        user.setFavoriteTeams(new HashSet<>());

        TeamPageDto team1 = new TeamPageDto();
        team1.setId(1L);
        TeamPageDto team2 = new TeamPageDto();
        team2.setId(2L);
        List<TeamPageDto> teams = Arrays.asList(team1, team2);

        when(teamService.findAllByIds(anyList())).thenReturn(teams);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

        userService.addTeamsToFavorites(Arrays.asList(1L, 2L), user);

        assertEquals(2, user.getFavoriteTeams().size());
        assertTrue(user.getFavoriteTeams().stream().anyMatch(team -> team.getId() == 1L));
        assertTrue(user.getFavoriteTeams().stream().anyMatch(team -> team.getId() == 2L));
    }

    @Test
    public void updateUsername_shouldThrowException_whenUsernameExists() {
        UserEditDto dto = new UserEditDto();
        dto.setUsername("existingUsername");

        when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new UserEntity()));

        assertThrows(IllegalStateException.class, () -> userService.updateUsername(dto, "existingUsername"));
    }

    @Test
    public void updateUsername_shouldUpdateUsername_whenValid() {
        UserEditDto dto = new UserEditDto();
        dto.setUsername("newUsername");

        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setUsername("oldUsername");

        when(userRepository.findByUsername("oldUsername")).thenReturn(Optional.of(existingUserEntity));
        when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());

        userService.updateUsername(dto, "oldUsername");

        assertEquals("newUsername", existingUserEntity.getUsername());
        verify(userRepository).save(existingUserEntity);
    }

    @Test
    public void getUser_shouldReturnNull_whenFindUsernameReturnsNull() {
        assertNull(userService.getUser());
    }

    @Test
    public void isUniqueUsername_shouldReturnTrue_whenUsernameIsUnique() {
        when(userRepository.findByUsername("uniqueUsername")).thenReturn(Optional.empty());

        boolean result = userService.isUniqueUsername("uniqueUsername");

        assertTrue(result);
    }

    @Test
    public void isUniqueUsername_shouldReturnFalse_whenUsernameExists() {
        when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new UserEntity()));

        boolean result = userService.isUniqueUsername("existingUsername");

        assertFalse(result);
    }

    @Test
    public void getFavoritePlayers_shouldReturnOnlyFavoritePlayers() {
        UserEntityPageDto user = new UserEntityPageDto();
        PlayerPageDto player1 = new PlayerPageDto();
        player1.setId(1L);
        PlayerPageDto player2 = new PlayerPageDto();
        player2.setId(2L);

        List<PlayerPageDto> allPlayers = Arrays.asList(player1, player2);

        PlayerPageDto favoritePlayer = new PlayerPageDto();
        favoritePlayer.setId(1L);

        user.setFavoritePlayers(new HashSet<>(Collections.singletonList(favoritePlayer)));

        List<PlayerPageDto> result = userService.getFavoritePlayers(allPlayers, user);

        assertEquals(1, result.size());
        assertTrue(result.contains(player1));
        assertFalse(result.contains(player2));
    }

    @Test
    public void isUniqueEmail_shouldReturnTrue_whenEmailIsUnique() {
        when(userRepository.findByEmail("unique@example.com")).thenReturn(Optional.empty());

        boolean result = userService.isUniqueEmail("unique@example.com");

        assertTrue(result);
    }

    @Test
    public void isUniqueEmail_shouldReturnFalse_whenEmailExists() {
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new UserEntity()));

        boolean result = userService.isUniqueEmail("existing@example.com");

        assertFalse(result);
    }

    @Test
    public void addPlayersToFavorites_shouldAddPlayers_whenNotAlreadyFavorite() {
        UserEntityPageDto user = new UserEntityPageDto();
        user.setFavoritePlayers(new HashSet<>());

        PlayerPageDto player1 = new PlayerPageDto();
        player1.setId(1L);
        PlayerPageDto player2 = new PlayerPageDto();
        player2.setId(2L);
        List<PlayerPageDto> players = Arrays.asList(player1, player2);

        when(playerService.getAllByIds(anyList())).thenReturn(players);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

        userService.addPlayersToFavorites(Arrays.asList(1L, 2L), user);

        assertEquals(2, user.getFavoritePlayers().size());
        assertTrue(user.getFavoritePlayers().stream().anyMatch(p -> p.getId() == 1L));
        assertTrue(user.getFavoritePlayers().stream().anyMatch(p -> p.getId() == 2L));
    }

    @Test
    public void removeFavoriteTeams_shouldRemoveTeams() {
        UserEntityPageDto user = new UserEntityPageDto();
        TeamPageDto team1 = new TeamPageDto();
        team1.setId(1L);
        TeamPageDto team2 = new TeamPageDto();
        team2.setId(2L);
        user.setFavoriteTeams(new HashSet<>(Arrays.asList(team1, team2)));

        List<TeamPageDto> teamsToRemove = List.of(team1);

        userService.removeFavoriteTeams(user, teamsToRemove);

        assertFalse(user.getFavoriteTeams().contains(team1));
        assertTrue(user.getFavoriteTeams().contains(team2));
    }

    @Test
    public void removeFavoritePlayers_shouldRemovePlayers() {
        UserEntityPageDto user = new UserEntityPageDto();
        PlayerPageDto player1 = new PlayerPageDto();
        player1.setId(1L);
        PlayerPageDto player2 = new PlayerPageDto();
        player2.setId(2L);
        user.setFavoritePlayers(new HashSet<>(Arrays.asList(player1, player2)));

        List<PlayerPageDto> playersToRemove = List.of(player1);

        userService.removeFavoritePlayers(user, playersToRemove);

        assertFalse(user.getFavoritePlayers().contains(player1));
        assertTrue(user.getFavoritePlayers().contains(player2));
    }

    @Test
    public void getFavoriteTeams_shouldReturnOnlyFavoriteTeams_whenFavoritesMatch() {
        UserEntityPageDto user = new UserEntityPageDto();

        TeamPageDto favoriteTeam1 = new TeamPageDto();
        favoriteTeam1.setId(1L);
        TeamPageDto favoriteTeam2 = new TeamPageDto();
        favoriteTeam2.setId(2L);
        user.setFavoriteTeams(new HashSet<>(Arrays.asList(favoriteTeam1, favoriteTeam2)));

        TeamPageDto team1 = new TeamPageDto();
        team1.setId(1L);
        TeamPageDto team2 = new TeamPageDto();
        team2.setId(2L);
        TeamPageDto team3 = new TeamPageDto();
        team3.setId(3L);
        List<TeamPageDto> allTeams = Arrays.asList(team1, team2, team3);

        List<TeamPageDto> result = userService.getFavoriteTeams(allTeams, user);

        assertEquals(2, result.size());
        assertTrue(result.contains(team1));
        assertTrue(result.contains(team2));
        assertFalse(result.contains(team3));
    }

    @Test
    public void getFavoriteTeams_shouldReturnEmptyList_whenNoFavoritesMatch() {
        UserEntityPageDto user = new UserEntityPageDto();

        TeamPageDto favoriteTeam1 = new TeamPageDto();
        favoriteTeam1.setId(1L);
        user.setFavoriteTeams(new HashSet<>(Arrays.asList(favoriteTeam1)));

        TeamPageDto team2 = new TeamPageDto();
        team2.setId(2L);
        TeamPageDto team3 = new TeamPageDto();
        team3.setId(3L);
        List<TeamPageDto> allTeams = Arrays.asList(team2, team3);

        List<TeamPageDto> result = userService.getFavoriteTeams(allTeams, user);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getFavoriteTeams_shouldReturnEmptyList_whenUserHasNoFavoriteTeams() {
        UserEntityPageDto user = new UserEntityPageDto();
        user.setFavoriteTeams(new HashSet<>());

        TeamPageDto team1 = new TeamPageDto();
        team1.setId(1L);
        TeamPageDto team2 = new TeamPageDto();
        team2.setId(2L);
        List<TeamPageDto> allTeams = Arrays.asList(team1, team2);

        List<TeamPageDto> result = userService.getFavoriteTeams(allTeams, user);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getFavoriteTeams_shouldReturnEmptyList_whenTeamsListIsEmpty() {
        UserEntityPageDto user = new UserEntityPageDto();

        TeamPageDto favoriteTeam1 = new TeamPageDto();
        favoriteTeam1.setId(1L);
        user.setFavoriteTeams(new HashSet<>(List.of(favoriteTeam1)));

        List<TeamPageDto> result = userService.getFavoriteTeams(new ArrayList<>(), user);

        assertTrue(result.isEmpty());
    }

    @Test
    public void addPlayersToFavorites_shouldNotAddPlayers_whenUserIsNull() {
        userService.addPlayersToFavorites(Arrays.asList(1L, 2L), null);
        verify(playerService, never()).getAllByIds(anyList());
    }

    @Test
    public void addPlayersToFavorites_shouldNotAddPlayers_whenPlayerIdsIsNull() {
        UserEntityPageDto user = new UserEntityPageDto();
        user.setFavoritePlayers(new HashSet<>());
        userService.addPlayersToFavorites(null, user);
        verify(playerService, never()).getAllByIds(anyList());
    }

    @Test
    public void addPlayersToFavorites_shouldNotAddPlayers_whenPlayerIdsIsEmpty() {
        UserEntityPageDto user = new UserEntityPageDto();
        user.setFavoritePlayers(new HashSet<>());

        userService.addPlayersToFavorites(Collections.emptyList(), user);
        verify(playerService, never()).getAllByIds(anyList());
    }
}
