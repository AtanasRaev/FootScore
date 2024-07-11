package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.dto.LeaguesPageDto;
import bg.softuni.footscore.model.entity.League;

import java.util.List;

public interface LeagueService {
    List<AddLeagueDto> getAllNotSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeagues();

    List<AddLeagueDto> getLeaguesByIds(List<Long> leagueIds);

    void saveSelectedLeagues(List<Long> leagueIds);

    void saveApiLeague(String name);

    boolean isEmpty();

    League getLeagueById(long leagueId);

    void saveLeague(League league);
}
