package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.AddLeagueDto;

import java.util.List;

public interface LeagueService {
    List<AddLeagueDto> getAllLeaguesByCountry(String name);

    List<AddLeagueDto> getAllNotSelectedLeaguesByCountry(String name);

    List<AddLeagueDto> getSelectedLeagues();

    void updateLeagues(String name, boolean selected);


    List<AddLeagueDto> getLeaguesByIds(List<Long> leagueIds);

    void saveSelectedLeagues(List<Long> leagueIds);
}
