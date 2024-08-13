package bg.softuni.footscore.service;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.repository.SeasonRepository;
import bg.softuni.footscore.service.impl.SeasonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeasonServiceImplTest {

    @Mock
    private SeasonRepository seasonRepository;

    @Mock
    private ApiConfig apiConfig;

    @Mock
    private RestClient restClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SeasonServiceImpl seasonService;

    @BeforeEach
    public void setup() {
        seasonService = new SeasonServiceImpl(seasonRepository, apiConfig, restClient, modelMapper);
    }

    @Test
    public void testGetAllSeasons() {
        List<Season> seasons = List.of(new Season());
        List<SeasonPageDto> expectedSeasons = List.of(new SeasonPageDto());

        when(seasonRepository.findAll()).thenReturn(seasons);
        when(modelMapper.map(any(Season.class), eq(SeasonPageDto.class))).thenReturn(new SeasonPageDto());

        List<SeasonPageDto> result = seasonService.getAllSeasons();

        assertEquals(expectedSeasons.size(), result.size());
    }

    @Test
    public void testIsEmptyWhenRepositoryIsEmpty() {
        when(seasonRepository.count()).thenReturn(0L);

        boolean result = seasonService.isEmpty();

        assertTrue(result);
    }

    @Test
    public void testIsEmptyWhenRepositoryIsNotEmpty() {
        when(seasonRepository.count()).thenReturn(5L);

        boolean result = seasonService.isEmpty();

        assertFalse(result);
    }

    @Test
    public void testGetSeasonByYearWhenFound() {
        int seasonYear = 2022;
        Season season = new Season();
        SeasonPageDto seasonPageDto = new SeasonPageDto();

        when(seasonRepository.findByYear(seasonYear)).thenReturn(Optional.of(season));
        when(modelMapper.map(season, SeasonPageDto.class)).thenReturn(seasonPageDto);

        SeasonPageDto result = seasonService.getSeasonByYear(seasonYear);

        assertNotNull(result);
        assertEquals(seasonPageDto, result);
    }

    @Test
    public void testGetSeasonByYearWhenNotFound() {
        int seasonYear = 2022;

        when(seasonRepository.findByYear(seasonYear)).thenReturn(Optional.empty());

        SeasonPageDto result = seasonService.getSeasonByYear(seasonYear);

        assertNull(result);
    }

    @Test
    public void testGetSeasonByIdWhenFound() {
        Long id = 1L;
        Season season = new Season();
        SeasonPageDto seasonPageDto = new SeasonPageDto();

        when(seasonRepository.findById(id)).thenReturn(Optional.of(season));
        when(modelMapper.map(season, SeasonPageDto.class)).thenReturn(seasonPageDto);

        SeasonPageDto result = seasonService.getSeasonById(id);

        assertNotNull(result);
        assertEquals(seasonPageDto, result);
    }

    @Test
    public void testGetSeasonByIdWhenNotFound() {
        Long id = 1L;

        when(seasonRepository.findById(id)).thenReturn(Optional.empty());

        SeasonPageDto result = seasonService.getSeasonById(id);

        assertNull(result);
    }
}
