package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.teamDto.TeamVenueDto;

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
