package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.leagueDto.LeagueApiDto;

import java.util.List;

public class LeagueCountrySeasonsApiDto {
    private LeagueApiDto league;
    private CountryApiDto country;
    private List<SeasonApiDto> seasons;

    public LeagueCountrySeasonsApiDto() {
    }

    public LeagueApiDto getLeague() {
        return league;
    }

    public void setLeague(LeagueApiDto league) {
        this.league = league;
    }

    public CountryApiDto getCountry() {
        return country;
    }

    public void setCountry(CountryApiDto country) {
        this.country = country;
    }

    public List<SeasonApiDto> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<SeasonApiDto> seasons) {
        this.seasons = seasons;
    }
}