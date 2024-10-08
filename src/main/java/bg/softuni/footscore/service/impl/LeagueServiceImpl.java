package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.LeagueCountrySeasonsApiDto;
import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.dto.leagueDto.LeagueAddDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.leagueDto.SelectedLeaguesDto;
import bg.softuni.footscore.model.entity.Country;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.LeagueRepository;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.exception.LeagueNotFoundException;
import jakarta.persistence.EntityNotFoundException;
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
    public List<LeaguePageDto> getAllSelectedLeaguesByCountry(String name, boolean selected) {
        List<LeaguePageDto> list = new ArrayList<>();
        this.leagueRepository.findByCountryNameAndSelectedNot(name, selected)
                .forEach(l -> {
                    LeaguePageDto map = this.modelMapper.map(l, LeaguePageDto.class);
                    list.add(map);
                });
        return list;
    }

    @Override
    public List<LeaguePageDto> getAllSelectedLeaguesDto() {
        List<LeaguePageDto> list = new ArrayList<>();
        this.leagueRepository.findBySelectedTrue()
                .forEach(l -> {
                    LeaguePageDto map = this.modelMapper.map(l, LeaguePageDto.class);
                    list.add(map);
                });
        return list;
    }

    @Override
    public SelectedLeaguesDto getAllSelectedWithCountry(String countryName) {
        List<String> countries = this.countryService.getAllCountriesNames();

        if (isEmpty()) {
            throw new LeagueNotFoundException("No leagues found for the specified country: " + countryName);
        }

        List<LeaguePageDto> allSelectedLeagues = "All countries".equals(countryName) || countryName.isEmpty() ?
                getAllSelectedLeaguesDto() :
                getAllSelectedLeaguesByCountry(countryName, false);
        SelectedLeaguesDto selectedLeaguesDto = new SelectedLeaguesDto();

        selectedLeaguesDto.setAllSelectedLeagues(allSelectedLeagues);
        selectedLeaguesDto.setCountries(countries);

        return selectedLeaguesDto;
    }

    @Override
    public List<LeaguePageDto> getLeagueByIds(List<Long> leagueIds) {
        if (leagueIds == null || leagueIds.isEmpty()) {
            throw new EntityNotFoundException("League Ids cannot be empty");
        }
        return this.leagueRepository.findAllById(leagueIds)
                .stream()
                .map(league -> this.modelMapper.map(league, LeaguePageDto.class))
                .toList();
    }

    @Override
    @Transactional
    public void updateSelectedLeagues(List<LeaguePageDto> leagues) {
        if (leagues == null || leagues.isEmpty()) {
            throw new EntityNotFoundException("Leagues not found");
        }

        List<League> list = leagues.stream().map(leagueDto -> {
            League league = this.modelMapper.map(leagueDto, League.class);

            Country country = countryService.getCountry(leagueDto.getCountry().getName())
                    .orElseThrow(() -> new EntityNotFoundException("Country not found"));

            league.setCountry(country);
            league.setSelected(true);
            return league;
        }).toList();

        this.leagueRepository.saveAll(list);
    }

    @Override
    @Transactional
    public void saveApiLeagues(String countryName) {
        ResponseCountryLeagueSeasonsApiDto responseDto = seasonService.getResponse(countryName);
        if (responseDto == null || responseDto.getResponse() == null) {
            throw new IllegalArgumentException("Response or leagues list is null");
        }

        List<League> leagues = responseDto.getResponse().stream()
                .map(this::convertToLeague)
                .collect(Collectors.toList());

        leagueRepository.saveAll(leagues);
    }

    private League convertToLeague(LeagueCountrySeasonsApiDto responseDto) {
        League league = new League();
        league.setName(responseDto.getLeague().getName());
        league.setLogo(responseDto.getLeague().getLogo());
        league.setApiId(responseDto.getLeague().getId());

        Country country = countryService.getCountry(responseDto.getCountry().getName())
                .orElseThrow(() -> new EntityNotFoundException("Country not found: " + responseDto.getCountry().getName()));
        league.setCountry(country);

        return league;
    }

    @Override
    public boolean isEmpty() {
        return this.leagueRepository.count() == 0;
    }

    @Override
    public LeaguePageDto getLeagueById(Long leagueId) {
        return this.leagueRepository
                .findById(leagueId)
                .map(league -> this.modelMapper.map(league, LeaguePageDto.class))
                .orElseThrow(()-> new LeagueNotFoundException("League not found"));
    }


    @Override
    public LeaguePageDto getLeagueByApiId(Long leagueApiId) {
        return this.leagueRepository
                .findByApiId(leagueApiId)
                .map(league -> this.modelMapper.map(league, LeaguePageDto.class))
                .orElse(null);
    }

    @Override
    public void updateLeague(League league) {
        this.leagueRepository.save(league);
    }

    @Override
    public List<LeaguePageDto> getAllLeagues() {
        return this.leagueRepository.findAll().stream().map(l -> this.modelMapper.map(l, LeaguePageDto.class)).toList();
    }

    @Override
    public void removeLeague(Long leagueId) {
        Optional<League> league = this.leagueRepository.findById(leagueId);
        if (league.isEmpty()) {
            throw new EntityNotFoundException("League not found with id: " + leagueId);
        }
        league.get().setSelected(false);
        updateLeague(league.get());
    }

    @Override
    public List<LeaguePageDto> getSelectedLeagueByTeamAndSeason(List<LeagueTeamSeasonPageDto> byTeamIdAndSeasonId) {
        List<LeaguePageDto> leagues = new ArrayList<>();
        byTeamIdAndSeasonId.forEach(s -> {
            LeaguePageDto leagueById = getLeagueById(s.getLeague().getId());
            if (leagueById.isSelected()) {
                leagues.add(leagueById);
            }
        });
        return leagues;
    }

    private List<LeagueAddDto> mapToAddLeagueDtoList(List<League> leagues) {
        List<LeagueAddDto> dtoList = new ArrayList<>();
        leagues.forEach(league -> dtoList.add(this.modelMapper.map(league, LeagueAddDto.class)));
        return dtoList;
    }
}

