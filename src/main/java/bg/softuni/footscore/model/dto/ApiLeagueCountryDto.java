package bg.softuni.footscore.model.dto;

public class ApiLeagueCountryDto {
    private ApiLeagueDto league;
    private ApiCountryDto country;

    public ApiLeagueCountryDto() {
    }

    public ApiLeagueDto getLeague() {
        return league;
    }

    public void setLeague(ApiLeagueDto league) {
        this.league = league;
    }

    public ApiCountryDto getCountry() {
        return country;
    }

    public void setCountry(ApiCountryDto country) {
        this.country = country;
    }
}