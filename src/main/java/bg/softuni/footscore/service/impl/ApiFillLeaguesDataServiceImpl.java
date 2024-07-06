package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ApiResponseCountryLeagueDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.CountryRepository;
import bg.softuni.footscore.repository.LeaguesRepository;
import bg.softuni.footscore.service.ApiFillLeaguesDataService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiFillLeaguesDataServiceImpl implements ApiFillLeaguesDataService {
    private final LeaguesRepository leaguesRepository;
    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;
    private final ModelMapper modelMapper;
    private final ApiFillCountryDataImpl fillCountryDataImpl;

    public ApiFillLeaguesDataServiceImpl(LeaguesRepository leaguesRepository, RestTemplate restTemplate, ApiConfig apiConfig, ModelMapper modelMapper, ApiFillCountryDataImpl fillCountryDataImpl) {
        this.leaguesRepository = leaguesRepository;
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
        this.modelMapper = modelMapper;
        this.fillCountryDataImpl = fillCountryDataImpl;
    }

    @Override
    public boolean hasData() {
        return this.leaguesRepository.count() > 0;
    }


    @Override
    @Transactional
    public void saveLeague(String name) {
        ApiResponseCountryLeagueDto response = this.fillCountryDataImpl.getDto(name);

        Country country = this.modelMapper.map(this.fillCountryDataImpl.getCountry(name), Country.class);

        List<League> leagues = response.getResponse().stream()
                .map(dto -> {
                    League league = this.modelMapper.map(dto.getLeague(), League.class);
                    league.setCountry(country);
                    country.getLeagues().add(league);
                    return league;
                })
                .collect(Collectors.toList());

        this.leaguesRepository.saveAll(leagues);
    }
}
