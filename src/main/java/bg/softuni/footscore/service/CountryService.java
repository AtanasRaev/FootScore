package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ApiResponseCountryLeagueDto;
import bg.softuni.footscore.model.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    List<String> getAllCountriesNames();

    ApiResponseCountryLeagueDto getResponse(String name);

    void saveCountry(String name);

    Optional<Country> getCountry(String name);

    boolean isEmpty();
}
