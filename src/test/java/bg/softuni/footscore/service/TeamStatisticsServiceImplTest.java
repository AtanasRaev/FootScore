package bg.softuni.footscore.service;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.teamDto.FormationDto;
import bg.softuni.footscore.model.entity.TeamStatistics;
import bg.softuni.footscore.repository.TeamStatisticsRepository;
import bg.softuni.footscore.service.impl.TeamStatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TeamStatisticsServiceImplTest {

    @Mock
    private TeamStatisticsRepository teamStatisticsRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private SeasonService seasonService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ApiConfig apiConfig;

    @Mock
    private RestClient restClient;

    @Mock
    private LeagueService leagueService;

    @InjectMocks
    private TeamStatisticsServiceImpl teamStatisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFormationsNoData() {
        when(teamStatisticsRepository.findAll()).thenReturn(List.of());
        List<FormationDto> result = teamStatisticsService.getAllFormations();
        assertEquals(0, result.size());
    }


    @Test
    void testGetAllFormations() {
        TeamStatistics teamStatistics = new TeamStatistics();
        teamStatistics.getLineups().put("4-4-2", 10);
        when(teamStatisticsRepository.findAll()).thenReturn(List.of(teamStatistics));
        List<FormationDto> result = teamStatisticsService.getAllFormations();

        assertEquals(1, result.size());
        assertEquals("4-4-2", result.getFirst().getFormation());
    }
}
