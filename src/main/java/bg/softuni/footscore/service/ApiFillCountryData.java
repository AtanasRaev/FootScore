package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ApiResponseCountryLeagueDto;
import bg.softuni.footscore.model.entity.Country;

import java.util.Optional;

public interface ApiFillCountryData {
    boolean hasData();

    ApiResponseCountryLeagueDto getDto(String name);

    void saveCountry(String name);

    Optional<Country> getCountry(String name);
}
