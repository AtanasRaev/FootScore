package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.playerDto.PlayerDetailsApiDto;
import bg.softuni.footscore.model.dto.teamDto.TeamApiDto;

import java.util.List;

public class TeamPlayerDto {
    private TeamApiDto team;
    private List<PlayerDetailsApiDto> players;

    public TeamApiDto getTeam() {
        return team;
    }

    public void setTeam(TeamApiDto team) {
        this.team = team;
    }

    public List<PlayerDetailsApiDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDetailsApiDto> players) {
        this.players = players;
    }
}
