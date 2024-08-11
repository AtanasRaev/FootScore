package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeagueAddDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.leagueDto.SelectedLeaguesDto;
import bg.softuni.footscore.model.entity.League;

import java.util.List;

public interface LeagueService {
    List<LeagueAddDto> getAllNotSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguePageDto> getAllSelectedLeaguesByCountry(String name, boolean selected);

    List<LeaguePageDto> getAllSelectedLeaguesDto();

    SelectedLeaguesDto getAllSelectedWithCountry(String countryName);

    List<LeaguePageDto> getLeagueByIds(List<Long> leagueIds);

    void updateSelectedLeagues(List<LeaguePageDto> leagues);

    void saveApiLeagues(String name);

    boolean isEmpty();

    LeaguePageDto getLeagueById(Long leagueId);

    LeaguePageDto getLeagueByApiId(Long leagueApiId);

    void updateLeague(League league);

    List<LeaguePageDto> getAllLeagues();

    void removeLeague(Long leagueId);

    List<LeaguePageDto> getSelectedLeagueByTeamAndSeason(List<LeagueTeamSeasonPageDto> byTeamIdAndSeasonId);
}
