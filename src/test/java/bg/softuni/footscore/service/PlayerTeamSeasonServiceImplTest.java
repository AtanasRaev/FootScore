package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.model.entity.PlayerTeamSeason;
import bg.softuni.footscore.repository.PlayerTeamSeasonRepository;
import bg.softuni.footscore.service.impl.PlayerTeamSeasonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerTeamSeasonServiceImplTest {

    @Mock
    private PlayerTeamSeasonRepository seasonTeamPlayerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PlayerTeamSeasonServiceImpl playerTeamSeasonService;

    private PlayerTeamSeason playerTeamSeason;
    private PlayerTeamSeasonPageDto playerTeamSeasonPageDto;

    @BeforeEach
    public void setUp() {
        playerTeamSeason = new PlayerTeamSeason();

        playerTeamSeasonPageDto = new PlayerTeamSeasonPageDto();
    }

    @Test
    public void testGetByTeamIdAndSeasonId() {
        Long teamId = 1L;
        Long seasonId = 1L;

        when(seasonTeamPlayerRepository.findByTeamIdAndSeasonId(teamId, seasonId))
                .thenReturn(Collections.singletonList(playerTeamSeason));
        when(modelMapper.map(playerTeamSeason, PlayerTeamSeasonPageDto.class))
                .thenReturn(playerTeamSeasonPageDto);

        List<PlayerTeamSeasonPageDto> result = playerTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(playerTeamSeasonPageDto, result.getFirst());

        verify(seasonTeamPlayerRepository).findByTeamIdAndSeasonId(teamId, seasonId);
        verify(modelMapper).map(playerTeamSeason, PlayerTeamSeasonPageDto.class);
    }

    @Test
    public void testSave() {
        playerTeamSeasonService.save(playerTeamSeason);

        verify(seasonTeamPlayerRepository).save(playerTeamSeason);
    }
}
