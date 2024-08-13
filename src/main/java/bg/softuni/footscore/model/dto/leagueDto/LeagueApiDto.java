package bg.softuni.footscore.model.dto.leagueDto;

public class LeagueApiDto {
    private long id;
    private String name;
    private String logo;
    private Integer season;

    public LeagueApiDto() {
    }

    public LeagueApiDto(long l, String league1) {
        this.id = l;
        this.name = league1;
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

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }
}
