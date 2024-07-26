package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.teamDto.TeamStatisticsDetailsDto;

public class ResponseTeamStatisticsSeason {
    TeamStatisticsDetailsDto response;

    public TeamStatisticsDetailsDto getResponse() {
        return response;
    }

    public void setResponse(TeamStatisticsDetailsDto response) {
        this.response = response;
    }
}
