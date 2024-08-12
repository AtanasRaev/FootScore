package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.teamDto.VenuePageDto;
import bg.softuni.footscore.model.entity.Venue;
import bg.softuni.footscore.repository.VenueRepository;
import bg.softuni.footscore.service.impl.VenueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VenueServiceImplTest {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VenueServiceImpl venueService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveAll_shouldMapDtosToEntitiesAndSave() {
        VenuePageDto dto1 = new VenuePageDto();
        VenuePageDto dto2 = new VenuePageDto();
        List<VenuePageDto> dtoList = List.of(dto1, dto2);

        Venue venue1 = new Venue();
        Venue venue2 = new Venue();
        when(modelMapper.map(dto1, Venue.class)).thenReturn(venue1);
        when(modelMapper.map(dto2, Venue.class)).thenReturn(venue2);

        venueService.saveAll(dtoList);

        verify(modelMapper).map(dto1, Venue.class);
        verify(modelMapper).map(dto2, Venue.class);
        verify(venueRepository).saveAll(List.of(venue1, venue2));
    }
}
