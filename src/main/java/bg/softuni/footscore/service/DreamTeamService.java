package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.DreamTeamPageDto;
import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.entity.DreamTeam;

import java.util.List;
import java.util.Optional;

public interface DreamTeamService {
    void create(String teamName, String formation, List<PlayerPageDto> allSelectedPlayers, UserEntityPageDto user);

    List<DreamTeamPageDto> getAllDreamTeamsByUserId(Long id);

    boolean checkTeamName(String teamName);
}
