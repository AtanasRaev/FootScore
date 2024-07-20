package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.LeagueAddDto;
import bg.softuni.footscore.model.dto.LeaguesPageDto;
import bg.softuni.footscore.model.entity.League;

import java.util.List;
import java.util.Optional;

public interface LeagueService {
    List<LeagueAddDto> getAllNotSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeaguesDto();

    List<League> getAllSelectedLeagues();

    List<LeagueAddDto> getLeaguesByIds(List<Long> leagueIds);

    League getLeaguesByIds(long leagueId);

    void saveSelectedLeagues(List<Long> leagueIds);

    void saveApiLeagues(String name);

    boolean isEmpty();

    Optional<League> getLeagueById(long leagueId);

    League getLeagueByApiId(long leagueApiId);

    void saveLeague(League league);

    List<League> getAllLeagues();
}
