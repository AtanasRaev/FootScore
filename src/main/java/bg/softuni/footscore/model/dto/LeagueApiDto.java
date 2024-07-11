package bg.softuni.footscore.model.dto;

public class LeagueApiDto {
    private String name;
    private String logo;

    public LeagueApiDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
