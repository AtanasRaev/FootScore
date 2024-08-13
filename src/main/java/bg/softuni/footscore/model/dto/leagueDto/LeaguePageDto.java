package bg.softuni.footscore.model.dto.leagueDto;

import bg.softuni.footscore.model.dto.countryDto.CountryApiDto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaguePageDto that = (LeaguePageDto) o;
        return getId() == that.getId() && isSelected() == that.isSelected() && getApiId() == that.getApiId() && Objects.equals(getName(), that.getName()) && Objects.equals(getLogo(), that.getLogo()) && Objects.equals(getCountry(), that.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLogo(), isSelected(), getApiId(), getCountry());
    }
}
