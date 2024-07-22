package bg.softuni.footscore.model.dto;
import java.util.List;

public class ResponseCountryLeagueSeasonsApiDto {

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