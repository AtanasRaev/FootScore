package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    List<String> getAllCountriesNames();

    void saveCountry(String name);

    Optional<Country> getCountry(String name);

    boolean isEmpty();
}
