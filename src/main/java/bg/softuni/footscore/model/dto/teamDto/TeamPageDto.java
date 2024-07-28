package bg.softuni.footscore.model.dto.teamDto;

import java.util.List;

public class TeamPageDto {
    private long id;
    private String name;
    private String logo;
    private long apiId;
    private VenuePageDto venue;
    private List<TeamStatisticsPageDto> statistics;

    public TeamPageDto(String name, String logo, VenuePageDto venue, long apiId) {
        this.name = name;
        this.logo = logo;
        this.venue = venue;
        this.apiId = apiId;
    }

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

    public List<TeamStatisticsPageDto> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<TeamStatisticsPageDto> statistics) {
        this.statistics = statistics;
    }
}
