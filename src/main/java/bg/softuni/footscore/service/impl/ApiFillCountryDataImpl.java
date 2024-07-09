package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ApiCountryDto;
import bg.softuni.footscore.model.dto.ApiLeagueCountryDto;
import bg.softuni.footscore.model.dto.ApiResponseCountryLeagueDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.repository.CountryRepository;
import bg.softuni.footscore.service.ApiFillCountryData;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class ApiFillCountryDataImpl implements ApiFillCountryData {
    private final CountryRepository countryRepository;
    private final ApiConfig apiConfig;
    private final RestClient restClient;
    private final ModelMapper modelMapper;

    public ApiFillCountryDataImpl(CountryRepository countryRepository, ApiConfig apiConfig, RestClient restClient, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponseCountryLeagueDto getResponse(String name) {
        String url = this.apiConfig.getUrl() + "leagues?country=" + name;

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(ApiResponseCountryLeagueDto.class);
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
        ApiResponseCountryLeagueDto response = getResponse(name);

        ApiCountryDto countryDto = response.getResponse().stream().findFirst()
                .map(ApiLeagueCountryDto::getCountry)
                .orElseThrow(() -> new IllegalStateException("No country data found"));

        Country country = this.modelMapper.map(countryDto, Country.class);
        this.countryRepository.save(country);
    }
}