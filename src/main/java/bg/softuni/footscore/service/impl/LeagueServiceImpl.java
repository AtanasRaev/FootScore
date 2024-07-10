package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.dto.ApiResponseCountryLeagueDto;
import bg.softuni.footscore.model.dto.LeaguesPageDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.LeagueRepository;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeagueServiceImpl implements LeagueService {
    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;
    private final CountryService countryService;

    public LeagueServiceImpl(LeagueRepository leagueRepository, ModelMapper modelMapper, CountryService countryService) {
        this.leagueRepository = leagueRepository;
        this.modelMapper = modelMapper;
        this.countryService = countryService;
    }

    @Override
    public List<AddLeagueDto> getAllNotSelectedLeaguesByCountry(String name, boolean selected) {
        List<League> leagues = this.leagueRepository.findByCountryNameAndSelectedNot(name, selected);
        return mapToAddLeagueDtoList(leagues);
    }

    @Override
    public List<LeaguesPageDto> getAllSelectedLeaguesByCountry(String name, boolean selected) {
        List<LeaguesPageDto> list = new ArrayList<>();
        this.leagueRepository.findByCountryNameAndSelectedNot(name, selected)
                .forEach(l -> {
                    LeaguesPageDto map = this.modelMapper.map(l, LeaguesPageDto.class);
                    map.setCountryName(name);
                    list.add(map);
                });
        return list;
    }

    @Override
    public List<LeaguesPageDto> getAllSelectedLeagues() {
        List<LeaguesPageDto> list = new ArrayList<>();
       this.leagueRepository.findBySelectedTrue()
               .forEach(l -> {
                   LeaguesPageDto map = this.modelMapper.map(l, LeaguesPageDto.class);
                   map.setCountryName(l.getCountry().getName());
                   list.add(map);
               });

       return list;
    }

    @Override
    public List<AddLeagueDto> getLeaguesByIds(List<Long> leagueIds) {
        List<AddLeagueDto> selectedLeagues = new ArrayList<>();
        leagueIds.forEach(id -> {
            Optional<League> league = this.leagueRepository.findById(id);
            selectedLeagues.add(this.modelMapper.map(league.get(), AddLeagueDto.class));
        });
        return selectedLeagues;
    }

    @Override
    public void saveSelectedLeagues(List<Long> leagueIds) {
        List<League> leaguesToSave = new ArrayList<>();
        leagueIds.forEach(id -> {
            Optional<League> league = this.leagueRepository.findById(id);
            league.get().setSelected(true);
            leaguesToSave.add(league.get());
        });
        this.leagueRepository.saveAll(leaguesToSave);
    }


    @Override
    @Transactional
    public void saveLeague(String name) {
        ApiResponseCountryLeagueDto response = this.countryService.getResponse(name);

        Country country = this.modelMapper.map(this.countryService.getCountry(name), Country.class);

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

    @Override
    public boolean isEmpty() {
        return this.leagueRepository.count() == 0;
    }

    private List<AddLeagueDto> mapToAddLeagueDtoList(List<League> leagues) {
        List<AddLeagueDto> dtoList = new ArrayList<>();
        leagues.forEach(league -> dtoList.add(this.modelMapper.map(league, AddLeagueDto.class)));
        return dtoList;
    }
}

