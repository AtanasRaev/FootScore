package bg.softuni.footscore.model.dto.teamDto;

public class LineupDto {
    private String formation;
    private Integer played;

    public LineupDto(String formation, Integer played) {
        this.formation = formation;
        this.played = played;
    }

    public LineupDto() {
    }

    public String getFormation() { return formation; }
    public void setFormation(String formation) { this.formation = formation; }

    public Integer getPlayed() { return played; }
    public void setPlayed(Integer played) { this.played = played; }
}