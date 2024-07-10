package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.dto.LeaguesPageDto;

import java.util.List;

public interface LeagueService {
    List<AddLeagueDto> getAllNotSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeagues();

    List<AddLeagueDto> getLeaguesByIds(List<Long> leagueIds);

    void saveSelectedLeagues(List<Long> leagueIds);

    void saveLeague(String name);

    boolean isEmpty();
}
