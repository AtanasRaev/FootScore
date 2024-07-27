package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.leagueDto.LeagueAddDto;
import bg.softuni.footscore.model.dto.LeagueCountrySeasonsApiDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguesPageDto;
import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.LeagueRepository;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeagueServiceImpl implements LeagueService {
    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;
    private final CountryService countryService;
    private final SeasonService seasonService;

    public LeagueServiceImpl(LeagueRepository leagueRepository,
                             ModelMapper modelMapper,
                             CountryService countryService,
                             SeasonService seasonService) {
        this.leagueRepository = leagueRepository;
        this.modelMapper = modelMapper;
        this.countryService = countryService;
        this.seasonService = seasonService;
    }

    @Override
    public List<LeagueAddDto> getAllNotSelectedLeaguesByCountry(String name, boolean selected) {
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
    public List<LeaguesPageDto> getAllSelectedLeaguesDto() {
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
    public List<League> getAllSelectedLeagues() {
        return this.leagueRepository.findBySelectedTrue();
    }

    @Override
    public List<LeagueAddDto> getLeagueByIds(List<Long> leagueIds) {
        List<LeagueAddDto> selectedLeagues = new ArrayList<>();
        leagueIds.forEach(id -> {
            Optional<League> league = this.leagueRepository.findById(id);
            selectedLeagues.add(this.modelMapper.map(league.get(), LeagueAddDto.class));
        });
        return selectedLeagues;
    }

    @Override
    public void updateSelectedLeagues(List<Long> leagueIds) {
        List<League> leaguesToSave = new ArrayList<>();
        leagueIds.forEach(id -> {
            Optional<League> league = this.leagueRepository.findById(id);
            if (league.isPresent()) {
                league.get().setSelected(true);
                leaguesToSave.add(league.get());
            }
        });
        this.leagueRepository.saveAll(leaguesToSave);
    }


    @Override
    @Transactional
    public void saveApiLeagues(String name) {
        ResponseCountryLeagueSeasonsApiDto response = this.seasonService.getResponse(name);

        Optional<Country> optionalCountry = this.countryService.getCountry(name);

        if (optionalCountry.isPresent()) {
            Country country = optionalCountry.get();

            List<League> leaguesToSave = new ArrayList<>();

            response.getResponse().forEach(dto -> {
                League league = new League(dto.getLeague().getName(),
                        dto.getLeague().getLogo(),
                        country,
                        false,
                        dto.getLeague().getId());

                Optional<League> optional = this.leagueRepository.findByApiId(league.getApiId());

                if (optional.isEmpty()) {
                    leaguesToSave.add(league);
                }
            });

            leaguesToSave.forEach(league -> {
                Optional<League> optional = this.leagueRepository.findByApiId(league.getApiId());

                optional.ifPresent(leaguesToSave::remove);
            });

            if (!leaguesToSave.isEmpty()) {
                country.setLeagues(leaguesToSave);
                this.leagueRepository.saveAll(leaguesToSave);
            }

        } else {
            throw new EntityNotFoundException("Country not found: " + name);
        }
    }

    private League mapToLeague(LeagueCountrySeasonsApiDto dto, Country country) {
        League league = this.modelMapper.map(dto.getLeague(), League.class);
        league.setCountry(country);
        league.setSelected(false);
        league.setApiId(dto.getLeague().getId());
        return league;
    }


    @Override
    public boolean isEmpty() {
        return this.leagueRepository.count() == 0;
    }

    @Override
    public Optional<League> getLeagueById(long leagueId) {
        return this.leagueRepository.findById(leagueId);
    }

    @Override
    public Optional<League> getLeagueByApiId(long leagueApiId) {
        return this.leagueRepository.findByApiId(leagueApiId);
    }

    @Override
    public void updateLeague(League league) {
        this.leagueRepository.save(league);
    }

    @Override
    public List<League> getAllLeagues() {
        return this.leagueRepository.findAll();
    }

    private List<LeagueAddDto> mapToAddLeagueDtoList(List<League> leagues) {
        List<LeagueAddDto> dtoList = new ArrayList<>();
        leagues.forEach(league -> dtoList.add(this.modelMapper.map(league, LeagueAddDto.class)));
        return dtoList;
    }
}

