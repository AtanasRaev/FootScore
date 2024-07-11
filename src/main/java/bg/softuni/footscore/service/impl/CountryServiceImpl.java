package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.CountryApiDto;
import bg.softuni.footscore.model.dto.LeagueCountrySeasonsApiDto;
import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.repository.CountryRepository;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.SeasonService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final ApiConfig apiConfig;
    private final RestClient restClient;
    private final ModelMapper modelMapper;
    private final SeasonService seasonService;

    public CountryServiceImpl(CountryRepository countryRepository, ApiConfig apiConfig, RestClient restClient, ModelMapper modelMapper, SeasonService seasonService) {
        this.countryRepository = countryRepository;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
        this.modelMapper = modelMapper;
        this.seasonService = seasonService;
    }

    @Override
    public List<String> getAllCountriesNames() {
        List<String> countries = new ArrayList<>();
        this.countryRepository.findAll().forEach(c -> countries.add(c.getName()));
        return countries;
    }

    @Override
    @Transactional
    public Optional<Country> getCountry(String name) {
        return this.countryRepository.findByName(name);
    }

    @Override
    public boolean isEmpty() {
        return this.countryRepository.count() == 0;
    }

    @Override
    @Transactional
    public void saveCountry(String name) {
        ResponseCountryLeagueSeasonsApiDto response = this.seasonService.getResponse(name);

        List<Season> seasons = this.seasonService.getAllSeasons();

        CountryApiDto countryDto = response.getResponse().stream().findFirst()
                .map(LeagueCountrySeasonsApiDto::getCountry)
                .orElseThrow(() -> new IllegalStateException("No country data found"));

        Country country = this.modelMapper.map(countryDto, Country.class);
        country.setSeasons(seasons);
        this.countryRepository.save(country);
    }
}
