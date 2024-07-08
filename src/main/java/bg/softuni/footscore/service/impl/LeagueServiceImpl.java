package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.repository.LeagueRepository;
import bg.softuni.footscore.service.LeagueService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueServiceImpl implements LeagueService {
    private final LeagueRepository leagueRepository;
    private final ModelMapper modelMapper;

    public LeagueServiceImpl(LeagueRepository leagueRepository, ModelMapper modelMapper) {
        this.leagueRepository = leagueRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AddLeagueDto> getAllLeaguesByCountry(String name) {
        return map(this.leagueRepository.findByCountryName(name));
    }

    @Override
    public List<AddLeagueDto> getSelectedLeagues() {
        return map(this.leagueRepository.findBySelected(true));
    }

    @Override
    public void updateLeagues(String name, boolean selected) {
        this.leagueRepository.updateLeagueBySelectedStatusByName(name, selected);
    }

    private List<AddLeagueDto> map (List<League> leagues) {
        return leagues.stream()
                .map(l -> this.modelMapper.map(l, AddLeagueDto.class))
                .toList();
    }
}
