package bg.softuni.footscore.model.dto.teamDto;

import java.util.List;

public class LineupDto {
    private String formation;
    private Integer played;

    public String getFormation() { return formation; }
    public void setFormation(String formation) { this.formation = formation; }

    public Integer getPlayed() { return played; }
    public void setPlayed(Integer played) { this.played = played; }
}