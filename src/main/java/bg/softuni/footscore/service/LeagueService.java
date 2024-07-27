package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.leagueDto.LeagueAddDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguesPageDto;
import bg.softuni.footscore.model.entity.League;

import java.util.List;
import java.util.Optional;

public interface LeagueService {
    List<LeagueAddDto> getAllNotSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguesPageDto> getAllSelectedLeaguesDto();

    List<League> getAllSelectedLeagues();

    List<LeagueAddDto> getLeagueByIds(List<Long> leagueIds);

    void updateSelectedLeagues(List<Long> leagueIds);

    void saveApiLeagues(String name);

    boolean isEmpty();

    Optional<League> getLeagueById(long leagueId);

    Optional<League> getLeagueByApiId(long leagueApiId);

    void updateLeague(League league);

    List<League> getAllLeagues();
}
