package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.DreamTeamPageDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.entity.DreamTeam;
import bg.softuni.footscore.repository.DreamTeamRepository;
import bg.softuni.footscore.service.impl.DreamTeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DreamTeamServiceImplTest {

    @Mock
    private DreamTeamRepository dreamTeamRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DreamTeamServiceImpl dreamTeamService;

    @Test
    public void createDreamTeam_ShouldCreateAndSaveDreamTeam() {
        String teamName = "Best Team";
        PlayerPageDto player1 = new PlayerPageDto(); player1.setId(1L); player1.setPosition("Defender");
        PlayerPageDto player2 = new PlayerPageDto(); player2.setId(2L); player2.setPosition("Midfielder");
        UserEntityPageDto user = new UserEntityPageDto();
        List<PlayerPageDto> players = List.of(player1, player2);
        String formation = "1-1-1";
        DreamTeamPageDto dreamTeamDto = new DreamTeamPageDto(teamName, formation, players, user);

        when(playerService.getAllSelectedPlayers(true)).thenReturn(players);
        when(userService.getUser()).thenReturn(user);
        when(modelMapper.map(any(DreamTeamPageDto.class), eq(DreamTeam.class))).thenReturn(new DreamTeam());

        dreamTeamService.createDreamTeam(teamName);

        verify(playerService).setSelected(1L, false);
        verify(playerService).setSelected(2L, false);
        verify(dreamTeamRepository).save(any(DreamTeam.class));
    }

    @Test
    public void getAllDreamTeamsByUserId_ShouldReturnDreamTeams() {
        Long userId = 1L;
        DreamTeam dreamTeam = new DreamTeam();
        DreamTeamPageDto dreamTeamDto = new DreamTeamPageDto();
        when(dreamTeamRepository.findAllByUserId(userId)).thenReturn(List.of(dreamTeam));
        when(modelMapper.map(dreamTeam, DreamTeamPageDto.class)).thenReturn(dreamTeamDto);

        List<DreamTeamPageDto> result = dreamTeamService.getAllDreamTeamsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(dreamTeamDto, result.getFirst());
    }

    @Test
    public void getById_ShouldReturnDreamTeamDto_WhenDreamTeamExists() {
        Long dreamTeamId = 1L;
        DreamTeam dreamTeam = new DreamTeam();
        DreamTeamPageDto dreamTeamDto = new DreamTeamPageDto();
        when(dreamTeamRepository.findById(dreamTeamId)).thenReturn(Optional.of(dreamTeam));
        when(modelMapper.map(dreamTeam, DreamTeamPageDto.class)).thenReturn(dreamTeamDto);

        DreamTeamPageDto result = dreamTeamService.getById(dreamTeamId);

        assertNotNull(result);
        assertEquals(dreamTeamDto, result);
    }

    @Test
    public void getById_ShouldReturnNull_WhenDreamTeamDoesNotExist() {
        Long dreamTeamId = 1L;
        when(dreamTeamRepository.findById(dreamTeamId)).thenReturn(Optional.empty());

        DreamTeamPageDto result = dreamTeamService.getById(dreamTeamId);

        assertNull(result);
    }

    @Test
    public void getAll_ShouldReturnAllDreamTeams() {
        DreamTeam dreamTeam = new DreamTeam();
        DreamTeamPageDto dreamTeamDto = new DreamTeamPageDto();
        when(dreamTeamRepository.findAll()).thenReturn(List.of(dreamTeam));
        when(modelMapper.map(dreamTeam, DreamTeamPageDto.class)).thenReturn(dreamTeamDto);

        List<DreamTeamPageDto> result = dreamTeamService.getAll();

        assertEquals(1, result.size());
        assertEquals(dreamTeamDto, result.getFirst());
    }

    @Test
    void testDeleteTeam_whenDreamTeamNotFound() {
        Long teamId = 1L;

        when(dreamTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> dreamTeamService.deleteTeam(teamId),
                "DreamTeam not found with ID: " + teamId);

        verify(dreamTeamRepository, times(0)).delete(any(DreamTeam.class));
    }

    @Test
    void deleteTeam_shouldHandleNullCase() {
        Long teamId = 1L;

        when(dreamTeamService.getById(teamId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> dreamTeamService.deleteTeam(teamId));

        verify(dreamTeamRepository, never()).delete(any());
    }

    @Test
    public void createPosition_ShouldReturnCorrectFormation() {
        PlayerPageDto defender = new PlayerPageDto(); defender.setPosition("Defender");
        PlayerPageDto midfielder = new PlayerPageDto(); midfielder.setPosition("Midfielder");
        PlayerPageDto attacker = new PlayerPageDto(); attacker.setPosition("Attacker");
        List<PlayerPageDto> players = List.of(defender, midfielder, attacker, defender);

        String result = dreamTeamService.createPosition(players);

        assertEquals("2-1-1", result);
    }
}
