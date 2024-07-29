package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;

import java.util.List;

public interface DreamTeamService {
    void create(String teamName, String formation, List<PlayerPageDto> allSelectedPlayers, UserEntityPageDto user);
}
