package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.AddLeagueDto;

import java.util.List;

public interface LeagueService {
    List<AddLeagueDto> getAllNotSelectedLeaguesByCountry(String name);

    List<AddLeagueDto> getLeaguesByIds(List<Long> leagueIds);

    void saveSelectedLeagues(List<Long> leagueIds);
}
