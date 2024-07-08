package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.AddLeagueDto;

import java.util.List;

public interface LeagueService {
    List<AddLeagueDto> getAllLeaguesByCountry(String country);

    List<AddLeagueDto> getSelectedLeagues();

    void updateLeagues(String name, boolean selected);
}
