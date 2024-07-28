package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.teamDto.VenuePageDto;
import bg.softuni.footscore.model.entity.Venue;
import bg.softuni.footscore.repository.VenueRepository;
import bg.softuni.footscore.service.VenueService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {
    private final VenueRepository venueRepository;
    private final ModelMapper modelMapper;

    public VenueServiceImpl(VenueRepository venueRepository, ModelMapper modelMapper) {
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveVenue(VenuePageDto venue) {
        this.venueRepository.save(this.modelMapper.map(venue, Venue.class));
    }

    @Override
    public void saveAll(List<VenuePageDto> venuesToSave) {
        this.venueRepository.saveAll(venuesToSave.stream().map(v -> this.modelMapper.map(v, Venue.class)).toList());
    }
}
