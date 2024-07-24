package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponsePlayerApiDto;
import bg.softuni.footscore.model.dto.SeasonsByPlayerApiDto;
import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;

import java.util.Optional;

public interface PlayerService {
    void saveApiPlayersForTeamAndSeason(Team team, Season season);

    ResponsePlayerApiDto getResponsePlayerApiDto(String query, long id, int seasonYear, int page);

    SeasonsByPlayerApiDto getResponseSeasonsByPlayerApiDto(String query, long id);

    Optional<Player> getPlayerByApiId(long apiId);

    boolean isEmpty();
}
