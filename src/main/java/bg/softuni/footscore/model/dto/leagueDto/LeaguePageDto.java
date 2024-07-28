package bg.softuni.footscore.model.dto.leagueDto;

import bg.softuni.footscore.model.dto.countryDto.CountryApiDto;

public class LeaguePageDto {
    private long id;
    private String name;
    private String logo;
    private boolean selected;
    private long apiId;
    private CountryApiDto country;

    public LeaguePageDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public CountryApiDto getCountry() {
        return country;
    }

    public void setCountry(CountryApiDto country) {
        this.country = country;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getApiId() {
        return apiId;
    }

    public void setApiId(long apiId) {
        this.apiId = apiId;
    }
}
