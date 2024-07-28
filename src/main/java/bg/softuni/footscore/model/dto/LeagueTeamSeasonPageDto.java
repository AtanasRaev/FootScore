package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

public class LeagueTeamSeasonPageDto {
    private TeamPageDto team;
    private SeasonPageDto season;
    private LeaguePageDto league;

    public LeagueTeamSeasonPageDto() {
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

    public LeaguePageDto getLeague() {
        return league;
    }

    public void setLeague(LeaguePageDto league) {
        this.league = league;
    }
}
