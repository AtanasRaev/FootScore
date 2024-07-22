package bg.softuni.footscore.model.dto;

import java.util.List;

public class ResponsePlayerApiDto {
    private List<PlayerStatisticsApiDto> response;

    public ResponsePlayerApiDto() {
    }

    public List<PlayerStatisticsApiDto> getResponse() {
        return response;
    }

    public void setResponse(List<PlayerStatisticsApiDto> response) {
        this.response = response;
    }
}
