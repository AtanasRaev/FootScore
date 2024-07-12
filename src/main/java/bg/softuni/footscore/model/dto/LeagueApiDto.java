package bg.softuni.footscore.model.dto;

public class LeagueApiDto {
    private long id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
