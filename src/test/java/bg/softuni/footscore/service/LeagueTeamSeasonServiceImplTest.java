package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.repository.LeagueTeamSeasonRepository;
import bg.softuni.footscore.service.impl.LeagueTeamSeasonServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LeagueTeamSeasonServiceImplTest {

    @InjectMocks
    private LeagueTeamSeasonServiceImpl service;

    @Mock
    private LeagueTeamSeasonRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllByLeagueIdAndSeasonId() {
        long leagueId = 1L;
        long seasonId = 2024L;
        LeagueTeamSeason entity = new LeagueTeamSeason();
        LeagueTeamSeasonPageDto dto = new LeagueTeamSeasonPageDto();
        when(repository.findByLeagueIdAndSeasonId(leagueId, seasonId)).thenReturn(List.of(entity));
        when(modelMapper.map(entity, LeagueTeamSeasonPageDto.class)).thenReturn(dto);

        List<LeagueTeamSeasonPageDto> result = service.getAllByLeagueIdAndSeasonId(leagueId, seasonId);

        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
    }

    @Test
    public void testGetByLeagueIdAndSeasonId() {
        long leagueId = 2L;
        long seasonId = 2023L;
        LeagueTeamSeason entity = new LeagueTeamSeason();
        LeagueTeamSeasonPageDto dto = new LeagueTeamSeasonPageDto();
        when(repository.findByLeagueIdAndSeasonId(leagueId, seasonId)).thenReturn(List.of(entity));
        when(modelMapper.map(entity, LeagueTeamSeasonPageDto.class)).thenReturn(dto);

        List<LeagueTeamSeasonPageDto> result = service.getByLeagueIdAndSeasonId(leagueId, seasonId);

        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
    }

    @Test
    public void testGetByTeamIdAndSeasonId() {
        long teamId = 3L;
        long seasonId = 2022L;
        LeagueTeamSeason entity = new LeagueTeamSeason();
        LeagueTeamSeasonPageDto dto = new LeagueTeamSeasonPageDto();
        when(repository.findByTeamIdAndSeasonId(teamId, seasonId)).thenReturn(List.of(entity));
        when(modelMapper.map(entity, LeagueTeamSeasonPageDto.class)).thenReturn(dto);

        List<LeagueTeamSeasonPageDto> result = service.getByTeamIdAndSeasonId(teamId, seasonId);

        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
    }

    @Test
    @Transactional
    public void testSave() {
        LeagueTeamSeason entity = new LeagueTeamSeason();
        service.save(entity);
    }
}
