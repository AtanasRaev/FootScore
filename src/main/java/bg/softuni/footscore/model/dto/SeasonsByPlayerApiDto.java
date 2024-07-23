package bg.softuni.footscore.model.dto;

import java.util.List;

public class SeasonsByPlayerApiDto {
    private List<Integer> response;

    public SeasonsByPlayerApiDto() {
    }

    public List<Integer> getResponse() {
        return response;
    }

    public void setResponse(List<Integer> response) {
        this.response = response;
    }
}
