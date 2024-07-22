package bg.softuni.footscore.model.dto;

public class StatisticsApiDto {
    private TeamApiDto team;

    private GameApiDto games;

    public StatisticsApiDto() {
    }

    public TeamApiDto getTeam() {
        return team;
    }

    public void setTeam(TeamApiDto team) {
        this.team = team;
    }

    public GameApiDto getGames() {
        return games;
    }

    public void setGames(GameApiDto games) {
        this.games = games;
    }
}
