package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.LeagueCountrySeasonsApiDto;
import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.dto.countryDto.CountryApiDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.repository.CountryRepository;
import bg.softuni.footscore.service.impl.CountryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SeasonService seasonService;

    @InjectMocks
    private CountryServiceImpl countryService;

    public CountryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveCountry_ShouldSaveCountry_WhenCountryDoesNotExist() {
        String countryName = "TestCountry";

        ResponseCountryLeagueSeasonsApiDto responseDto = new ResponseCountryLeagueSeasonsApiDto();
        LeagueCountrySeasonsApiDto leagueCountrySeasonsApiDto = new LeagueCountrySeasonsApiDto();
        CountryApiDto countryApiDto = new CountryApiDto();
        countryApiDto.setName(countryName);
        leagueCountrySeasonsApiDto.setCountry(countryApiDto);
        responseDto.setResponse(List.of(leagueCountrySeasonsApiDto));

        Country country = new Country();
        country.setName(countryName);

        when(seasonService.getResponse(countryName)).thenReturn(responseDto);
        when(modelMapper.map(countryApiDto, Country.class)).thenReturn(country);
        when(countryRepository.findByName(countryName)).thenReturn(Optional.empty());

        countryService.saveCountry(countryName);

        verify(countryRepository).save(country);
    }

    @Test
    public void saveCountry_ShouldNotSaveCountry_WhenCountryAlreadyExists() {
        String countryName = "TestCountry";

        ResponseCountryLeagueSeasonsApiDto responseDto = new ResponseCountryLeagueSeasonsApiDto();
        LeagueCountrySeasonsApiDto leagueCountrySeasonsApiDto = new LeagueCountrySeasonsApiDto();
        CountryApiDto countryApiDto = new CountryApiDto();
        countryApiDto.setName(countryName);
        leagueCountrySeasonsApiDto.setCountry(countryApiDto);
        responseDto.setResponse(List.of(leagueCountrySeasonsApiDto));

        Country country = new Country();
        country.setName(countryName);

        when(seasonService.getResponse(countryName)).thenReturn(responseDto);
        when(modelMapper.map(countryApiDto, Country.class)).thenReturn(country);
        when(countryRepository.findByName(countryName)).thenReturn(Optional.of(country));

        countryService.saveCountry(countryName);

        verify(countryRepository, never()).save(any(Country.class));
    }
    @Test
    public void getAllCountriesNames_ShouldReturnListOfCountryNames() {
        Country country1 = new Country();
        country1.setName("Country1");
        Country country2 = new Country();
        country2.setName("Country2");

        List<Country> countryList = List.of(country1, country2);

        when(countryRepository.findAll()).thenReturn(countryList);

        List<String> countryNames = countryService.getAllCountriesNames();

        assertEquals(2, countryNames.size());
        assertTrue(countryNames.contains("Country1"));
        assertTrue(countryNames.contains("Country2"));
    }

    @Test
    public void getCountry_ShouldReturnCountry_WhenCountryExists() {
        String countryName = "ExistingCountry";
        Country country = new Country();
        country.setName(countryName);

        when(countryRepository.findByName(countryName)).thenReturn(Optional.of(country));

        Optional<Country> result = countryService.getCountry(countryName);

        assertTrue(result.isPresent());
        assertEquals(countryName, result.get().getName());
    }

    @Test
    public void getCountry_ShouldReturnEmptyOptional_WhenCountryDoesNotExist() {
        String countryName = "NonExistingCountry";

        when(countryRepository.findByName(countryName)).thenReturn(Optional.empty());

        Optional<Country> result = countryService.getCountry(countryName);

        assertFalse(result.isPresent());
    }

    @Test
    public void isEmpty_ShouldReturnTrue_WhenRepositoryIsEmpty() {
        when(countryRepository.count()).thenReturn(0L);

        boolean result = countryService.isEmpty();

        assertTrue(result);
    }

    @Test
    public void isEmpty_ShouldReturnFalse_WhenRepositoryIsNotEmpty() {
        when(countryRepository.count()).thenReturn(5L);

        boolean result = countryService.isEmpty();

        assertFalse(result);
    }
}
