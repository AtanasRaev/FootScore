package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.repository.LeagueTeamSeasonRepository;
import bg.softuni.footscore.service.LeagueTeamSeasonService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueTeamSeasonServiceImpl implements LeagueTeamSeasonService {
    private final LeagueTeamSeasonRepository seasonLeagueTeamRepository;
    private final ModelMapper modelMapper;

    public LeagueTeamSeasonServiceImpl(LeagueTeamSeasonRepository seasonLeagueTeamRepository,
                                       ModelMapper modelMapper) {
        this.seasonLeagueTeamRepository = seasonLeagueTeamRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LeagueTeamSeasonPageDto> getAllByLeagueIdAndSeasonId(Long leagueId, Long seasonId) {
        return this.seasonLeagueTeamRepository.findByLeagueIdAndSeasonId(leagueId, seasonId)
                .stream()
                .map(s -> this.modelMapper.map(s, LeagueTeamSeasonPageDto.class))
                .toList();
    }

    @Override
    public List<LeagueTeamSeasonPageDto> getByLeagueIdAndSeasonId(Long leagueId, Long seasonId) {
        return this.seasonLeagueTeamRepository.findByLeagueIdAndSeasonId(leagueId, seasonId)
                .stream()
                .map(s -> this.modelMapper.map(s, LeagueTeamSeasonPageDto.class))
                .toList();
    }

    @Override
    public List<LeagueTeamSeasonPageDto> getByTeamIdAndSeasonId(Long teamId, Long seasonId) {
        return this.seasonLeagueTeamRepository.findByTeamIdAndSeasonId(teamId, seasonId)
                .stream()
                .map(s -> this.modelMapper.map(s, LeagueTeamSeasonPageDto.class))
                .toList();
    }

    @Override
    @Transactional
    public void save(LeagueTeamSeason seasonLeagueTeam) {
        this.seasonLeagueTeamRepository.save(seasonLeagueTeam);
    }

}
