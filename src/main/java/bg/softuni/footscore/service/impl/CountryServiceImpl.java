package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.repository.CountryRepository;
import bg.softuni.footscore.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<String> getAllCountriesNames() {
        List<String> countries = new ArrayList<>();
        this.countryRepository.findAll().forEach(c -> countries.add(c.getName()));
        return countries;
    }
}
