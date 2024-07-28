package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

public class PlayerTeamSeasonPageDto {
    private long id;
    private TeamPageDto team;
    private SeasonPageDto season;
    private PlayerPageDto player;

    public PlayerTeamSeasonPageDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TeamPageDto getTeam() {
        return team;
    }

    public void setTeam(TeamPageDto team) {
        this.team = team;
    }

    public SeasonPageDto getSeason() {
        return season;
    }

    public void setSeason(SeasonPageDto season) {
        this.season = season;
    }

    public PlayerPageDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerPageDto player) {
        this.player = player;
    }
}
