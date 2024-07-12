package bg.softuni.footscore.model.dto;

public class TeamVenueDto {
    TeamApiDto team;
    VenueApiDto venue;

    public TeamVenueDto() {
    }

    public TeamApiDto getTeam() {
        return team;
    }

    public void setTeam(TeamApiDto team) {
        this.team = team;
    }

    public VenueApiDto getVenue() {
        return venue;
    }

    public void setVenue(VenueApiDto venue) {
        this.venue = venue;
    }
}
