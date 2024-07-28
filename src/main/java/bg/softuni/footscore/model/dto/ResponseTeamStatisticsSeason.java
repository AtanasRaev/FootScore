package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.teamDto.TeamStatisticsDetailsApiDto;

public class ResponseTeamStatisticsSeason {
    TeamStatisticsDetailsApiDto response;

    public TeamStatisticsDetailsApiDto getResponse() {
        return response;
    }

    public void setResponse(TeamStatisticsDetailsApiDto response) {
        this.response = response;
    }
}
