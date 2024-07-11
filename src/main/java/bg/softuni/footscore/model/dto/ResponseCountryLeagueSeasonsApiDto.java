package bg.softuni.footscore.model.dto;

import java.io.Serializable;
import java.util.List;

public class ResponseCountryLeagueSeasonsApiDto implements Serializable {

    private List<LeagueCountrySeasonsApiDto> response;

    public List<LeagueCountrySeasonsApiDto> getResponse() {
        return response;
    }

    public void setResponse(List<LeagueCountrySeasonsApiDto> response) {
        this.response = response;
    }

    public ResponseCountryLeagueSeasonsApiDto() {
    }
}