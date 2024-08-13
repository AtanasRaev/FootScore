package bg.softuni.footscore.service;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.repository.PlayerRepository;
import bg.softuni.footscore.service.impl.PlayerServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerTeamSeasonService playerTeamSeasonService;

    @Mock
    private SeasonService seasonService;

    @Mock
    private TeamService teamService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ApiConfig apiConfig;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player player;
    private PlayerPageDto playerPageDto;

    @BeforeEach
    public void setUp() {
        player = new Player(
                "First",
                "Last",
                "Name",
                25,
                LocalDate.of(1998, 8, 15),
                "Nationality",
                180,
                75,
                "photo.jpg",
                1L);
        playerPageDto = new PlayerPageDto();
        playerPageDto.setId(1L);
        playerPageDto.setFullName("First Last");
        playerPageDto.setPosition("Forward");
        playerPageDto.setShortName("F. Last");

    }

    @Test
    @Transactional
    public void testResetIsSelected_NoPlayers() {
        when(playerRepository.findAll()).thenReturn(Collections.emptyList());
        playerService.resetIsSelected();
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testGetAllPlayersByPosition_NoSelectedPlayers() {
        when(playerService.getAllSelectedPlayers(true)).thenReturn(Collections.emptyList());
        List<PlayerPageDto> result = playerService.getAllPlayersByPosition("Forward");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPlayersByPosition_EmptyList() {
        List<PlayerPageDto> players = new ArrayList<>();
        List<PlayerPageDto> result = playerService.getPlayersByPosition("Forward", players);
        assertTrue(result.isEmpty(), "The result should be an empty list when input list is empty.");
    }

    @Test
    public void testGetPlayersByPosition_AllPositions() {
        PlayerPageDto player1 = new PlayerPageDto();
        player1.setPosition("Forward");
        PlayerPageDto player2 = new PlayerPageDto();
        player2.setPosition("Defender");
        List<PlayerPageDto> players = Arrays.asList(player1, player2);
        List<PlayerPageDto> result = playerService.getPlayersByPosition("All positions", players);

        assertEquals(players, result);
    }

    @Test
    @Transactional
    public void testResetIsSelected() {
        Player player1 = new Player();
        Player player2 = new Player();
        player1.setSelected(true);
        player2.setSelected(true);

        when(playerRepository.findAll()).thenReturn(Arrays.asList(player1, player2));
        playerService.resetIsSelected();

        assertFalse(player1.isSelected());
        assertFalse(player2.isSelected());
        verify(playerRepository).save(player1);
        verify(playerRepository).save(player2);
    }

    @Test
    public void testGetPlayerByApiId() {
        when(playerRepository.findByApiId(1L)).thenReturn(Optional.of(player));
        when(modelMapper.map(player, PlayerPageDto.class)).thenReturn(playerPageDto);

        PlayerPageDto result = playerService.getPlayerByApiId(1L);
        assertNotNull(result);
        assertEquals(playerPageDto.getId(), result.getId());
    }

    @Test
    public void testIsEmpty() {
        when(playerRepository.count()).thenReturn(0L);
        assertTrue(playerService.isEmpty());
    }

    @Test
    public void testGetPlayerById() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(modelMapper.map(player, PlayerPageDto.class)).thenReturn(playerPageDto);

        PlayerPageDto result = playerService.getPlayerById(1L);
        assertNotNull(result);
        assertEquals(playerPageDto.getId(), result.getId());
    }

    @Test
    public void testGetAllByIds() {
        List<Player> players = Collections.singletonList(player);
        List<PlayerPageDto> dtos = Collections.singletonList(playerPageDto);

        when(playerRepository.findAllById(Collections.singletonList(1L))).thenReturn(players);
        when(modelMapper.map(player, PlayerPageDto.class)).thenReturn(playerPageDto);

        List<PlayerPageDto> result = playerService.getAllByIds(Collections.singletonList(1L));
        assertEquals(dtos, result);
    }

    @Test
    public void testGetAllSelectedPlayers() {
        List<Player> players = Collections.singletonList(player);
        List<PlayerPageDto> dtos = Collections.singletonList(playerPageDto);

        when(playerRepository.findByIsSelected(true)).thenReturn(players);
        when(modelMapper.map(player, PlayerPageDto.class)).thenReturn(playerPageDto);

        List<PlayerPageDto> result = playerService.getAllSelectedPlayers(true);
        assertEquals(dtos, result);
    }

    @Test
    public void testSetSelected() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.setSelected(1L, true);

        verify(playerRepository).save(player);
        assertTrue(player.isSelected());
    }

    @Test
    public void testGetPlayersByPosition() {
        PlayerPageDto playerPageDto = new PlayerPageDto();
        playerPageDto.setPosition("Forward");
        List<PlayerPageDto> players = new ArrayList<>();
        players.add(playerPageDto);

        List<PlayerPageDto> filteredPlayers = new ArrayList<>();
        filteredPlayers.add(playerPageDto);

        List<PlayerPageDto> result = playerService.getPlayersByPosition("Forward", players);
        assertEquals(filteredPlayers, result);
    }

    @Test
    @Transactional
    public void testGetAllPlayers() {
        long teamId = 1L;
        Long seasonId = 1L;
        PlayerTeamSeasonPageDto playerTeamSeasonPageDto = new PlayerTeamSeasonPageDto();
        playerTeamSeasonPageDto.setId(1L);
        playerTeamSeasonPageDto.setPlayer(new PlayerPageDto());
        playerTeamSeasonPageDto.setSeason(new SeasonPageDto());
        playerTeamSeasonPageDto.setTeam(new TeamPageDto());
        List<PlayerTeamSeasonPageDto> allPlayers = Collections.singletonList(playerTeamSeasonPageDto);
        TeamPageDto team = new TeamPageDto();

        List<PlayerTeamSeasonPageDto> result = playerService.getAllPlayers(teamId, seasonId, allPlayers, team);
        assertEquals(allPlayers, result);
    }
}
