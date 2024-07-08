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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ApiFillCountryDataImpl implements ApiFillCountryData {
    private final CountryRepository countryRepository;
    private final ApiConfig apiConfig;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    public ApiFillCountryDataImpl(CountryRepository countryRepository, ApiConfig apiConfig, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.apiConfig = apiConfig;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean hasData() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public ApiResponseCountryLeagueDto getDto(String name) {
        String url = this.apiConfig.getUrl() + "leagues?country=" + name;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", this.apiConfig.getKey());
        headers.set("x-rapidapi-host", this.apiConfig.getUrl());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponseCountryLeagueDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ApiResponseCountryLeagueDto.class
        );

        return response.getBody();
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
        ApiResponseCountryLeagueDto response = getDto(name);

        ApiCountryDto countryDto = response.getResponse().stream().findFirst()
                .map(ApiLeagueCountryDto::getCountry)
                .orElseThrow(() -> new IllegalStateException("No country data found"));

        Country country = this.modelMapper.map(countryDto, Country.class);
        this.countryRepository.save(country);
    }
}
