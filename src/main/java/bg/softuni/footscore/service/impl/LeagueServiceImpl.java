package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.LeagueRepository;
import bg.softuni.footscore.service.LeagueService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeagueServiceImpl implements LeagueService {
    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;

    public LeagueServiceImpl(LeagueRepository leagueRepository, ModelMapper modelMapper) {
        this.leagueRepository = leagueRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AddLeagueDto> getAllNotSelectedLeaguesByCountry(String name) {
        List<League> leagues = this.leagueRepository.findByCountryNameAndSelectedNot(name, true);
        return mapToDtoList(leagues);
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

    private List<AddLeagueDto> mapToDtoList(List<League> leagues) {
        List<AddLeagueDto> dtoList = new ArrayList<>();
        leagues.forEach(league -> dtoList.add(this.modelMapper.map(league, AddLeagueDto.class)));
        return dtoList;
    }
}

