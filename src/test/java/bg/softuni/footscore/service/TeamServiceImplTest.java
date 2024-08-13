package bg.softuni.footscore.service;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.teamDto.*;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.Venue;
import bg.softuni.footscore.repository.TeamRepository;
import bg.softuni.footscore.service.impl.TeamServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestClient;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TeamServiceImplTest {

    private TeamServiceImpl teamService;
    private TeamRepository teamRepository;
    private ModelMapper modelMapper;
    private LeagueTeamSeasonService leagueTeamSeasonService;
    private VenueService venueService;

    @BeforeEach
    void setUp() {
        teamRepository = mock(TeamRepository.class);
        VenueService venueService = mock(VenueService.class);
        modelMapper = mock(ModelMapper.class);
        ApiConfig apiConfig = mock(ApiConfig.class);
        RestClient restClient = mock(RestClient.class);
        LeagueTeamSeasonService leagueTeamSeasonService = mock(LeagueTeamSeasonService.class);

        teamService = new TeamServiceImpl(teamRepository, venueService, modelMapper, apiConfig, restClient, leagueTeamSeasonService);
    }

    @Test
    void testFetchTeams_withNullLeagues_throwsException() {
        List<SeasonPageDto> seasons = List.of(new SeasonPageDto());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.fetchTeams(null, seasons));

        assertEquals("Not found leagues or seasons", exception.getMessage());
    }

    @Test
    void testFetchTeams_withEmptyLeagues_throwsException() {
        List<SeasonPageDto> seasons = List.of(new SeasonPageDto());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.fetchTeams(Collections.emptyList(), seasons));

        assertEquals("Not found leagues or seasons", exception.getMessage());
    }

    @Test
    void testFetchTeams_withNullSeasons_throwsException() {
        List<LeaguePageDto> leagues = List.of(new LeaguePageDto());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.fetchTeams(leagues, null));

        assertEquals("Not found leagues or seasons", exception.getMessage());
    }

    @Test
    void testFetchTeams_withEmptySeasons_throwsException() {
        List<LeaguePageDto> leagues = List.of(new LeaguePageDto());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.fetchTeams(leagues, Collections.emptyList()));

        assertEquals("Not found leagues or seasons", exception.getMessage());
    }

    @Test
    void testFetchTeams_withEmptyLeaguesAndSeasons_throwsException() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.fetchTeams(Collections.emptyList(), Collections.emptyList()));

        assertEquals("Not found leagues or seasons", exception.getMessage());
    }

    @Test
    void testSaveApiTeamsForLeagueAndSeason_withNullLeague_throwsException() {
        SeasonPageDto season = new SeasonPageDto();
        season.setYear(2024);

        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> teamService.saveApiTeamsForLeagueAndSeason(null, season));

        assertEquals("League and Season are required", exception.getMessage());
    }

    @Test
    void testSaveApiTeamsForLeagueAndSeason_withNullSeason_throwsException() {
        LeaguePageDto league = new LeaguePageDto();
        league.setApiId(1L);

        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> teamService.saveApiTeamsForLeagueAndSeason(league, null));

        assertEquals("League and Season are required", exception.getMessage());
    }

    @Test
    void testSaveApiTeamsForLeagueAndSeason_withBothNull_throwsException() {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> teamService.saveApiTeamsForLeagueAndSeason(null, null));

        assertEquals("League and Season are required", exception.getMessage());
    }


    @Test
    void testUpdateTeam_whenVenueIsNull() {
        TeamPageDto teamDto = new TeamPageDto();
        teamDto.setVenue(null);
        Team team = new Team();
        when(modelMapper.map(any(TeamPageDto.class), eq(Team.class))).thenReturn(team);

        teamService.updateTeam(teamDto);

        verify(modelMapper, times(1)).map(teamDto, Team.class);
        verify(modelMapper, times(0)).map(any(VenuePageDto.class), eq(Venue.class));
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    void testFetchTeams_whenLeaguesAndSeasonsAreEmpty() {
        List<LeaguePageDto> leagues = List.of();
        List<SeasonPageDto> seasons = List.of();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.fetchTeams(leagues, seasons));

        assertEquals("Not found leagues or seasons", exception.getMessage());
    }

    @Test
    void testSaveApiTeamsForLeagueAndSeason_withNullParams_throwsException() {

        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> teamService.saveApiTeamsForLeagueAndSeason(null, null));

        assertEquals("League and Season are required", exception.getMessage());
    }


    @Test
    void testIsEmpty() {
        when(teamRepository.count()).thenReturn(0L);

        boolean isEmpty = teamService.isEmpty();

        assertTrue(isEmpty);
    }

    @Test
    void testFindById() {
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(modelMapper.map(any(), eq(TeamPageDto.class))).thenReturn(new TeamPageDto());

        TeamPageDto result = teamService.findById(teamId);

        assertNotNull(result);
    }

    @Test
    void testIsEmpty_whenRepositoryIsEmpty() {
        when(teamRepository.count()).thenReturn(0L);

        boolean isEmpty = teamService.isEmpty();

        assertTrue(isEmpty);
        verify(teamRepository, times(1)).count();
    }

    @Test
    void testIsEmpty_whenRepositoryIsNotEmpty() {
        when(teamRepository.count()).thenReturn(10L);

        boolean isEmpty = teamService.isEmpty();

        assertFalse(isEmpty);
        verify(teamRepository, times(1)).count();
    }

    @Test
    void testFindAllByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);

        Team team1 = new Team();
        team1.setId(1L);
        team1.setApiId(101L);
        Team team2 = new Team();
        team2.setId(2L);
        team2.setApiId(102L);

        when(teamRepository.findAllById(ids)).thenReturn(Arrays.asList(team1, team2));

        when(modelMapper.map(any(Team.class), eq(TeamPageDto.class)))
                .thenAnswer(invocation -> {
                    Team team = invocation.getArgument(0);
                    return new TeamPageDto(team.getName(), team.getLogo(), new VenuePageDto(), team.getApiId());
                });

        List<TeamPageDto> result = teamService.findAllByIds(ids);

        assertEquals(2, result.size());
        assertEquals(101L, result.get(0).getApiId());
        assertEquals(102L, result.get(1).getApiId());
        verify(teamRepository, times(1)).findAllById(ids);
    }


    @Test
    void testFindById_whenTeamExists() {
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(modelMapper.map(any(Team.class), eq(TeamPageDto.class)))
                .thenReturn(new TeamPageDto());

        TeamPageDto result = teamService.findById(teamId);

        assertNotNull(result);
        verify(teamRepository, times(1)).findById(teamId);
        verify(modelMapper, times(1)).map(team, TeamPageDto.class);
    }

    @Test
    void testFindById_whenTeamDoesNotExist() {
        Long teamId = 1L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamPageDto result = teamService.findById(teamId);

        assertNull(result);
        verify(teamRepository, times(1)).findById(teamId);
        verify(modelMapper, times(0)).map(any(), any());
    }

    @Test
    void testGetTeamByApiId_whenTeamExists() {
        long apiId = 1L;
        Team team = new Team();
        team.setApiId(apiId);
        when(teamRepository.findByApiId(apiId)).thenReturn(Optional.of(team));
        when(modelMapper.map(any(Team.class), eq(TeamPageDto.class)))
                .thenReturn(new TeamPageDto());

        TeamPageDto result = teamService.getTeamByApiId(apiId);

        assertNotNull(result);
        verify(teamRepository, times(1)).findByApiId(apiId);
        verify(modelMapper, times(1)).map(team, TeamPageDto.class);
    }

    @Test
    void testGetTeamByApiId_whenTeamDoesNotExist() {
        long apiId = 1L;
        when(teamRepository.findByApiId(apiId)).thenReturn(Optional.empty());

        TeamPageDto result = teamService.getTeamByApiId(apiId);

        assertNull(result);
        verify(teamRepository, times(1)).findByApiId(apiId);
        verify(modelMapper, times(0)).map(any(), any());
    }

    @Test
    void testGetTeamById_whenTeamExists() {
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(modelMapper.map(any(Team.class), eq(TeamPageDto.class)))
                .thenReturn(new TeamPageDto());

        TeamPageDto result = teamService.getTeamById(teamId);

        assertNotNull(result);
        verify(teamRepository, times(1)).findById(teamId);
        verify(modelMapper, times(1)).map(team, TeamPageDto.class);
    }

    @Test
    void testGetTeamById_whenTeamDoesNotExist() {
        Long teamId = 1L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamPageDto result = teamService.getTeamById(teamId);

        assertNull(result);
        verify(teamRepository, times(1)).findById(teamId);
        verify(modelMapper, times(0)).map(any(), any());
    }

    @Test
    void testUpdateTeam() {
        TeamPageDto teamDto = new TeamPageDto();
        teamDto.setVenue(new VenuePageDto());
        Team team = new Team();
        Venue venue = new Venue();
        when(modelMapper.map(any(TeamPageDto.class), eq(Team.class))).thenReturn(team);
        when(modelMapper.map(any(VenuePageDto.class), eq(Venue.class))).thenReturn(venue);

        teamService.updateTeam(teamDto);

        verify(modelMapper, times(1)).map(teamDto, Team.class);
        verify(modelMapper, times(1)).map(teamDto.getVenue(), Venue.class);
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    void testSaveAll() {
        TeamPageDto teamDto1 = new TeamPageDto();
        TeamPageDto teamDto2 = new TeamPageDto();
        List<TeamPageDto> teamsToSave = Arrays.asList(teamDto1, teamDto2);

        Team team1 = new Team();
        Team team2 = new Team();
        when(modelMapper.map(teamDto1, Team.class)).thenReturn(team1);
        when(modelMapper.map(teamDto2, Team.class)).thenReturn(team2);

        teamService.saveAll(teamsToSave);

        verify(teamRepository, times(1)).saveAll(anyList());
        verify(modelMapper, times(1)).map(teamDto1, Team.class);
        verify(modelMapper, times(1)).map(teamDto2, Team.class);
    }
}
