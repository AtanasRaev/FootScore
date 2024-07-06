package bg.softuni.footscore.model.dto;

import java.io.Serializable;
import java.util.List;

public class ApiResponseCountryLeagueDto implements Serializable {

    private List<ApiLeagueCountryDto> response;

    public List<ApiLeagueCountryDto> getResponse() {
        return response;
    }

    public void setResponse(List<ApiLeagueCountryDto> response) {
        this.response = response;
    }

    public ApiResponseCountryLeagueDto() {
    }
}