package bg.softuni.footscore.model.dto.teamDto;

public class TeamApiDto {
    private long id;
    private String name;
    private String logo;

    public TeamApiDto() {
    }

    public TeamApiDto(String teamName, String teamLogo, long l) {
        this.name = teamName;
        this.logo = teamLogo;
        this.id = l;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
