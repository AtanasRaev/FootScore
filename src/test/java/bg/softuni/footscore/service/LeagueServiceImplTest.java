package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.LeagueCountrySeasonsApiDto;
import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.dto.countryDto.CountryApiDto;
import bg.softuni.footscore.model.dto.leagueDto.*;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.LeagueRepository;
import bg.softuni.footscore.service.exception.LeagueNotFoundException;
import bg.softuni.footscore.service.impl.LeagueServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeagueServiceImplTest {

    @Mock
    private LeagueRepository leagueRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CountryService countryService;

    @Mock
    private SeasonService seasonService;

    @InjectMocks
    private LeagueServiceImpl leagueServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        leagueRepository = mock(LeagueRepository.class);
        modelMapper = mock(ModelMapper.class);
        countryService = mock(CountryService.class);
        seasonService = mock(SeasonService.class);
        leagueServiceImpl = new LeagueServiceImpl(leagueRepository, modelMapper, countryService, seasonService);
    }

    @Test
    void getAllNotSelectedLeaguesByCountry_validCountry_returnsList() {
        String countryName = "Country";
        boolean selected = false;
        List<League> leagues = new ArrayList<>();
        League league = new League();
        league.setName("League1");
        leagues.add(league);

        when(leagueRepository.findByCountryNameAndSelectedNot(countryName, selected)).thenReturn(leagues);
        when(modelMapper.map(any(League.class), eq(LeagueAddDto.class))).thenReturn(new LeagueAddDto());

        List<LeagueAddDto> result = leagueServiceImpl.getAllNotSelectedLeaguesByCountry(countryName, selected);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leagueRepository).findByCountryNameAndSelectedNot(countryName, selected);
    }

    @Test
    void getAllSelectedLeaguesByCountry_validCountry_returnsList() {
        String countryName = "Country";
        boolean selected = false;
        List<League> leagues = new ArrayList<>();
        League league = new League();
        league.setName("League1");
        leagues.add(league);

        when(leagueRepository.findByCountryNameAndSelectedNot(countryName, selected)).thenReturn(leagues);
        when(modelMapper.map(any(League.class), eq(LeaguePageDto.class))).thenReturn(new LeaguePageDto());

        List<LeaguePageDto> result = leagueServiceImpl.getAllSelectedLeaguesByCountry(countryName, selected);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leagueRepository).findByCountryNameAndSelectedNot(countryName, selected);
    }

    @Test
    void testGetAllSelectedWithCountry_NoLeagues_ThrowsLeagueNotFoundException() {
        CountryService mockCountryService = mock(CountryService.class);
        LeagueServiceImpl leagueServiceImpl = spy(new LeagueServiceImpl(leagueRepository, modelMapper, countryService, seasonService));

        when(mockCountryService.getAllCountriesNames()).thenReturn(List.of("Country1", "Country2"));
        doReturn(true).when(leagueServiceImpl).isEmpty();

        LeagueNotFoundException thrownException = assertThrows(
                LeagueNotFoundException.class,
                () -> leagueServiceImpl.getAllSelectedWithCountry("SomeCountry")
        );

        assertEquals("No leagues found for the specified country: SomeCountry", thrownException.getMessage());
    }


    @Test
    void getAllSelectedLeaguesDto_returnsList() {
        List<League> leagues = new ArrayList<>();
        League league = new League();
        league.setName("League1");
        leagues.add(league);

        when(leagueRepository.findBySelectedTrue()).thenReturn(leagues);
        when(modelMapper.map(any(League.class), eq(LeaguePageDto.class))).thenReturn(new LeaguePageDto());

        List<LeaguePageDto> result = leagueServiceImpl.getAllSelectedLeaguesDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leagueRepository).findBySelectedTrue();
    }

    @Test
    void getAllSelectedWithCountry_validCountry_returnsSelectedLeaguesDto() {
        String countryName = "Country";
        List<String> countries = List.of("Country1", "Country2");
        List<League> leagueEntities = List.of(new League());
        LeaguePageDto leagueDto = new LeaguePageDto();
        List<LeaguePageDto> leagueDtos = List.of(leagueDto);

        when(countryService.getAllCountriesNames()).thenReturn(countries);

        when(leagueRepository.count()).thenReturn(1L);
        when(leagueRepository.findByCountryNameAndSelectedNot(countryName, false)).thenReturn(leagueEntities);

        when(modelMapper.map(any(League.class), eq(LeaguePageDto.class))).thenReturn(leagueDto);

        SelectedLeaguesDto result = leagueServiceImpl.getAllSelectedWithCountry(countryName);

        assertNotNull(result);
        assertEquals(countries, result.getCountries());
        assertEquals(leagueDtos.size(), result.getAllSelectedLeagues().size());
        for (int i = 0; i < leagueDtos.size(); i++) {
            LeaguePageDto expectedDto = leagueDtos.get(i);
            LeaguePageDto actualDto = result.getAllSelectedLeagues().get(i);
            assertEquals(expectedDto.getId(), actualDto.getId());
            assertEquals(expectedDto.getName(), actualDto.getName());
            assertEquals(expectedDto.isSelected(), actualDto.isSelected());
        }
    }

    @Test
    void testGetLeagueByIds_NullLeagueIds_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> leagueServiceImpl.getLeagueByIds(null));
    }

    @Test
    void testGetLeagueByIds_EmptyLeagueIds_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> leagueServiceImpl.getLeagueByIds(Collections.emptyList()));
    }


    @Test
    void getLeagueByIds_validIds_returnsLeaguePageDtoList() {
        List<Long> leagueIds = List.of(1L, 2L);
        List<League> leagues = List.of(new League(), new League());

        when(leagueRepository.findAllById(leagueIds)).thenReturn(leagues);
        when(modelMapper.map(any(League.class), eq(LeaguePageDto.class))).thenReturn(new LeaguePageDto());

        List<LeaguePageDto> result = leagueServiceImpl.getLeagueByIds(leagueIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(leagueRepository).findAllById(leagueIds);
    }

    @Test
    void updateSelectedLeagues_validLeagues_updatesLeagues() {
        Country country = new Country();
        country.setName("CountryName");

        LeaguePageDto leagueDto = new LeaguePageDto();
        leagueDto.setCountry(new CountryApiDto());
        leagueDto.getCountry().setName("CountryName");

        List<LeaguePageDto> leagueDtos = List.of(leagueDto);
        League league = new League();
        when(modelMapper.map(any(LeaguePageDto.class), eq(League.class))).thenReturn(league);
        when(countryService.getCountry(anyString())).thenReturn(Optional.of(country));

        leagueServiceImpl.updateSelectedLeagues(leagueDtos);

        verify(leagueRepository).saveAll(anyList());
    }

    @Test
    void testUpdateSelectedLeagues_NullLeagues_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> leagueServiceImpl.updateSelectedLeagues(null),
                "Leagues not found");
    }

    @Test
    void testUpdateSelectedLeagues_EmptyLeagues_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> leagueServiceImpl.updateSelectedLeagues(Collections.emptyList()),
                "Leagues not found");
    }


    @Test
    void saveApiLeagues_Success() {
        String countryName = "TestCountry";
        ResponseCountryLeagueSeasonsApiDto responseDto = getResponseCountryLeagueSeasonsApiDto();

        Country country = new Country();
        country.setName("TestCountry");

        when(seasonService.getResponse(countryName)).thenReturn(responseDto);
        when(countryService.getCountry(anyString())).thenReturn(Optional.of(country));

        leagueServiceImpl.saveApiLeagues(countryName);

        verify(seasonService).getResponse(countryName);
        verify(countryService).getCountry("TestCountry");
        verify(leagueRepository).saveAll(anyList());
    }

    @Test
    void saveApiLeagues_ResponseDtoIsNull_ThrowsException() {
        String countryName = "TestCountry";
        when(seasonService.getResponse(countryName)).thenReturn(null);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> leagueServiceImpl.saveApiLeagues(countryName)
        );
        assertTrue(thrown.getMessage().contains("Response or leagues list is null"));
    }

    @Test
    void saveApiLeagues_ResponseDtoWithNullLeagues_ThrowsException() {
        String countryName = "TestCountry";
        ResponseCountryLeagueSeasonsApiDto responseDto = new ResponseCountryLeagueSeasonsApiDto();
        responseDto.setResponse(null);

        when(seasonService.getResponse(countryName)).thenReturn(responseDto);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> leagueServiceImpl.saveApiLeagues(countryName)
        );
        assertTrue(thrown.getMessage().contains("Response or leagues list is null"));
    }

    @Test
    void saveApiLeagues_CountryNotFound_ThrowsException() {
        String countryName = "TestCountry";
        ResponseCountryLeagueSeasonsApiDto responseDto = getResponseCountryLeagueSeasonsApiDto();

        when(seasonService.getResponse(countryName)).thenReturn(responseDto);
        when(countryService.getCountry(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> leagueServiceImpl.saveApiLeagues(countryName)
        );
        assertTrue(thrown.getMessage().contains("Country not found: TestCountry"));
    }

    @Test
    void isEmpty_returnsTrueIfNoLeagues() {
        when(leagueRepository.count()).thenReturn(0L);

        boolean result = leagueServiceImpl.isEmpty();

        assertTrue(result);
    }

    @Test
    void getLeagueById_validId_returnsLeaguePageDto() {
        Long leagueId = 1L;
        League league = new League();
        LeaguePageDto dto = new LeaguePageDto();

        when(leagueRepository.findById(leagueId)).thenReturn(Optional.of(league));
        when(modelMapper.map(league, LeaguePageDto.class)).thenReturn(dto);

        LeaguePageDto result = leagueServiceImpl.getLeagueById(leagueId);

        assertNotNull(result);
        verify(leagueRepository).findById(leagueId);
    }

    @Test
    void getLeagueByApiId_validApiId_returnsLeaguePageDto() {
        long apiId = 1L;
        League league = new League();
        LeaguePageDto dto = new LeaguePageDto();

        when(leagueRepository.findByApiId(apiId)).thenReturn(Optional.of(league));
        when(modelMapper.map(league, LeaguePageDto.class)).thenReturn(dto);

        LeaguePageDto result = leagueServiceImpl.getLeagueByApiId(apiId);

        assertNotNull(result);
        verify(leagueRepository).findByApiId(apiId);
    }

    @Test
    void updateLeague_validLeague_savesLeague() {
        League league = new League();

        leagueServiceImpl.updateLeague(league);

        verify(leagueRepository).save(league);
    }

    @Test
    void getAllLeagues_returnsList() {
        List<League> leagues = List.of(new League());
        when(leagueRepository.findAll()).thenReturn(leagues);
        when(modelMapper.map(any(League.class), eq(LeaguePageDto.class))).thenReturn(new LeaguePageDto());

        List<LeaguePageDto> result = leagueServiceImpl.getAllLeagues();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leagueRepository).findAll();
    }

    @Test
    void removeLeague_validId_updatesLeague() {
        Long leagueId = 1L;
        League league = new League();
        league.setSelected(true);

        when(leagueRepository.findById(leagueId)).thenReturn(Optional.of(league));

        leagueServiceImpl.removeLeague(leagueId);

        assertFalse(league.isSelected());
        verify(leagueRepository).save(league);
    }

    @Test
    void testRemoveLeague_LeagueDoesNotExist_ThrowsException() {
        Long leagueId = 1L;
        when(leagueRepository.findById(leagueId)).thenReturn(Optional.empty());

        EntityNotFoundException thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> leagueServiceImpl.removeLeague(leagueId)
        );
        assertEquals("League not found with id: " + leagueId, thrownException.getMessage());
    }


    @Test
    void testGetSelectedLeagueByTeamAndSeason_WithEmptyInput() {
        List<LeagueTeamSeasonPageDto> inputList = new ArrayList<>();

        List<LeaguePageDto> result = leagueServiceImpl.getSelectedLeagueByTeamAndSeason(inputList);

        assertEquals(0, result.size());
    }


    private static ResponseCountryLeagueSeasonsApiDto getResponseCountryLeagueSeasonsApiDto() {
        CountryApiDto countryApiDto = new CountryApiDto();
        countryApiDto.setName("TestCountry");
        LeagueApiDto leagueApiDto = new LeagueApiDto();
        leagueApiDto.setName("LeagueName");
        leagueApiDto.setLogo("LeagueLogo");
        leagueApiDto.setId(1L);
        ResponseCountryLeagueSeasonsApiDto responseDto = new ResponseCountryLeagueSeasonsApiDto();
        LeagueCountrySeasonsApiDto leagueApiResponseDto = new LeagueCountrySeasonsApiDto();
        leagueApiResponseDto.setLeague(leagueApiDto);
        leagueApiResponseDto.setCountry(countryApiDto);
        responseDto.setResponse(List.of(leagueApiResponseDto));
        return responseDto;
    }
}