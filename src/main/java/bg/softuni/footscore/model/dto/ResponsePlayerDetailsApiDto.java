package bg.softuni.footscore.model.dto;

import java.util.List;

public class ResponsePlayerDetailsApiDto {
    private List<TeamPlayerDto> response;

    public List<TeamPlayerDto> getResponse() {
        return response;
    }

    public void setResponse(List<TeamPlayerDto> response) {
        this.response = response;
    }
}
