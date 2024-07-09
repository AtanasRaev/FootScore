package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.ApiResponseCountryLeagueDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.LeagueRepository;
import bg.softuni.footscore.service.ApiFillLeaguesDataService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiFillLeaguesDataServiceImpl implements ApiFillLeaguesDataService {
    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;
    private final ApiFillCountryDataImpl fillCountryDataImpl;

    public ApiFillLeaguesDataServiceImpl(LeagueRepository leaguesRepository, ModelMapper modelMapper, ApiFillCountryDataImpl fillCountryDataImpl) {
        this.leagueRepository = leaguesRepository;
        this.modelMapper = modelMapper;
        this.fillCountryDataImpl = fillCountryDataImpl;
    }

    @Override
    public boolean hasData() {
        return this.leagueRepository.count() > 0;
    }


    @Override
    @Transactional
    public void saveLeague(String name) {
        ApiResponseCountryLeagueDto response = this.fillCountryDataImpl.getResponse(name);

        Country country = this.modelMapper.map(this.fillCountryDataImpl.getCountry(name), Country.class);

        List<League> leagues = response.getResponse().stream()
                .map(dto -> {
                    League league = this.modelMapper.map(dto.getLeague(), League.class);
                    league.setCountry(country);
                    league.setSelected(false);
                    country.getLeagues().add(league);
                    return league;
                })
                .collect(Collectors.toList());

        this.leagueRepository.saveAll(leagues);
    }
}
