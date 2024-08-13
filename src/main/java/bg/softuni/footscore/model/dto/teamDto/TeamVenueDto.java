package bg.softuni.footscore.model.dto.teamDto;

public class TeamVenueDto {
    TeamApiDto team;
    VenueApiDto venue;

    public TeamVenueDto() {
    }

    public TeamVenueDto(TeamApiDto team, VenueApiDto venue) {
        this.team = team;
        this.venue = venue;
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
