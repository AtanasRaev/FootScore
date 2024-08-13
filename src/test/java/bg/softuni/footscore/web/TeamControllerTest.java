package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Mock
    private LeagueService leagueService;

    @Mock
    private SeasonService seasonService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private TeamController teamController;

    private LeaguePageDto leaguePageDto;
    private SeasonPageDto seasonPageDto;
    private UserEntityPageDto userEntityPageDto;

    @BeforeEach
    public void setUp() {
        leaguePageDto = new LeaguePageDto();
        leaguePageDto.setId(1L);

        seasonPageDto = new SeasonPageDto();
        seasonPageDto.setId(1L);
        seasonPageDto.setYear(2023);

        userEntityPageDto = new UserEntityPageDto();
    }

    @Test
    public void testAddTeam() {
        when(leagueService.getLeagueById(anyLong())).thenReturn(leaguePageDto);
        when(seasonService.getAllSeasons()).thenReturn(List.of(seasonPageDto));
        when(userService.getUser()).thenReturn(userEntityPageDto);

        String viewName = teamController.addTeam(1L, null, List.of(1L, 2L), model);

        verify(userService).addTeamsToFavorites(anyList(), any(UserEntityPageDto.class));
        assertEquals("redirect:/league/1/teams?seasonId=1", viewName);
    }
}
