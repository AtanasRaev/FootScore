package bg.softuni.footscore.model.dto.teamDto;

public class LineupDtoDetails {
    private String formation;

    private Integer played;

    public LineupDtoDetails() {
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public Integer getPlayed() {
        return played;
    }

    public void setPlayed(Integer played) {
        this.played = played;
    }
}
