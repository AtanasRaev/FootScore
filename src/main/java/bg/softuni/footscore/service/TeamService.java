package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.entity.Team;

public interface TeamService {
    void saveApiTeamsForLeagueAndSeason(long id);

    ResponseTeamApiDto getResponse(long leagueApiId, int seasonYear);

    Team findByName(String name);

    boolean isEmpty();

}
