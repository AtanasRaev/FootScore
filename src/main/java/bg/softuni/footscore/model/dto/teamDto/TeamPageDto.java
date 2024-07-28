package bg.softuni.footscore.model.dto.teamDto;

public class TeamPageDto {
    private long id;
    private String name;
    private String logo;
    private long apiId;
    private VenuePageDto venue;

    public TeamPageDto() {
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

    public long getApiId() {
        return apiId;
    }

    public void setApiId(long apiId) {
        this.apiId = apiId;
    }

    public VenuePageDto getVenue() {
        return venue;
    }

    public void setVenue(VenuePageDto venue) {
        this.venue = venue;
    }
}
