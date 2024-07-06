package bg.softuni.footscore.model.dto;

public class ApiLeagueDto {
    private String name;
    private String logo;

    public ApiLeagueDto() {
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
