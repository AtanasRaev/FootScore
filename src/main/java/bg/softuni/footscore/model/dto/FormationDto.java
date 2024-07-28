package bg.softuni.footscore.model.dto;

public class FormationDto {
    private String formation;

    public FormationDto() {
    }

    public FormationDto(String formation) {
        this.formation = formation;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }
}
