package bg.softuni.footscore.model.dto;

import java.util.List;

public class ResponseTeamApiDto {
    List<TeamVenueDto> response;

    public ResponseTeamApiDto() {
    }

    public List<TeamVenueDto> getResponse() {
        return response;
    }

    public void setResponse(List<TeamVenueDto> response) {
        this.response = response;
    }
}
