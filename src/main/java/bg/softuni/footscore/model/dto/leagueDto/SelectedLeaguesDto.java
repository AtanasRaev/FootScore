package bg.softuni.footscore.model.dto.leagueDto;

import java.util.List;

public class SelectedLeaguesDto {
    private List<String> countries;
    private List<LeaguePageDto> allSelectedLeagues;

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<LeaguePageDto> getAllSelectedLeagues() {
        return allSelectedLeagues;
    }

    public void setAllSelectedLeagues(List<LeaguePageDto> allSelectedLeagues) {
        this.allSelectedLeagues = allSelectedLeagues;
    }
}
