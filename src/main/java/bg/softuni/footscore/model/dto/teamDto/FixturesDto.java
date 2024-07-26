package bg.softuni.footscore.model.dto.teamDto;

public class FixturesDto {
    private StatisticsDto played;
    private StatisticsDto wins;
    private StatisticsDto draws;
    private StatisticsDto loses;

    // Getters and setters
    public StatisticsDto getPlayed() { return played; }
    public void setPlayed(StatisticsDto played) { this.played = played; }

    public StatisticsDto getWins() { return wins; }
    public void setWins(StatisticsDto wins) { this.wins = wins; }

    public StatisticsDto getDraws() { return draws; }
    public void setDraws(StatisticsDto draws) { this.draws = draws; }

    public StatisticsDto getLoses() { return loses; }
    public void setLoses(StatisticsDto loses) { this.loses = loses; }
}
