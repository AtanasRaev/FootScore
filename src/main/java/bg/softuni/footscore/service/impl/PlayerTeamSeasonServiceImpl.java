package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.repository.PlayerTeamSeasonRepository;
import bg.softuni.footscore.service.PlayerTeamSeasonService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerTeamSeasonServiceImpl implements PlayerTeamSeasonService {
    private final PlayerTeamSeasonRepository seasonTeamPlayerRepository;
    private final ModelMapper modelMapper;

    public PlayerTeamSeasonServiceImpl(PlayerTeamSeasonRepository seasonTeamPlayerRepository,
                                       ModelMapper modelMapper) {
        this.seasonTeamPlayerRepository = seasonTeamPlayerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PlayerTeamSeasonPageDto> getByTeamIdAndSeasonId(Long teamId, Long seasonId) {
        return this.seasonTeamPlayerRepository.findByTeamIdAndSeasonId(teamId, seasonId)
                .stream()
                .map(s -> this.modelMapper.map(s, PlayerTeamSeasonPageDto.class))
                .toList();
    }


    @Override
    public void save(bg.softuni.footscore.model.entity.PlayerTeamSeason seasonTeamPlayer) {
        this.seasonTeamPlayerRepository.save(seasonTeamPlayer);
    }
}
