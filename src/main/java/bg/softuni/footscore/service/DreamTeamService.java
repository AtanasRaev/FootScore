package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.teamDto.DreamTeamPageDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;

import java.util.List;

public interface DreamTeamService {
    void create(String teamName, String formation, List<PlayerPageDto> allSelectedPlayers, UserEntityPageDto user);

    List<DreamTeamPageDto> getAllDreamTeamsByUserId(Long id);

    boolean checkTeamName(String teamName);

    DreamTeamPageDto getById(Long dreamTeamId);

    List<DreamTeamPageDto> getAll();

    void deleteTeam(Long teamId);
}
