package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.teamDto.VenuePageDto;

import java.util.List;

public interface VenueService {
    void saveAll(List<VenuePageDto> venuesToSave);
}
