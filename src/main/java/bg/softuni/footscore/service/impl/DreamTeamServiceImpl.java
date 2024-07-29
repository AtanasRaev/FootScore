package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.DreamTeamPageDto;
import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.entity.DreamTeam;
import bg.softuni.footscore.repository.DreamTeamRepository;
import bg.softuni.footscore.service.DreamTeamService;
import bg.softuni.footscore.service.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DreamTeamServiceImpl implements DreamTeamService {
    private final DreamTeamRepository dreamTeamRepository;
    private final PlayerService playerService;
    private final ModelMapper modelMapper;

    public DreamTeamServiceImpl(DreamTeamRepository dreamTeamRepository,
                                PlayerService playerService,
                                ModelMapper modelMapper) {
        this.dreamTeamRepository = dreamTeamRepository;
        this.playerService = playerService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(String teamName, String formation, List<PlayerPageDto> allSelectedPlayers, UserEntityPageDto user) {
        DreamTeamPageDto dto = new DreamTeamPageDto(teamName, formation, allSelectedPlayers, user);
        dto.getPlayers().forEach(p -> this.playerService.setSelected(p.getId(), false));
        update(dto);
    }

    @Override
    public List<DreamTeamPageDto> getAllDreamTeamsByUserId(Long userId) {
        return this.dreamTeamRepository.findAllByUserId(userId).stream().map(dt -> this.modelMapper.map(dt, DreamTeamPageDto.class)).toList();
    }

    @Override
    public boolean checkTeamName(String teamName) {
        return this.dreamTeamRepository.findByName(teamName).isPresent();
    }

    private void update(DreamTeamPageDto dto) {
        this.dreamTeamRepository.save(this.modelMapper.map(dto, DreamTeam.class));
    }
}
