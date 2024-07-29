package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;

import java.util.List;
import java.util.Set;

public class DreamTeamPageDto {
    private String name;
    private String formation;
    private List<PlayerPageDto> players;
    private UserEntityPageDto userEntityPageDto;

    public DreamTeamPageDto() {
    }

    public DreamTeamPageDto(String name, String formation, List<PlayerPageDto> players, UserEntityPageDto userEntityPageDto) {
        this.name = name;
        this.formation = formation;
        this.players = players;
        this.userEntityPageDto = userEntityPageDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public List<PlayerPageDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerPageDto> players) {
        this.players = players;
    }

    public UserEntityPageDto getUserEntityPageDto() {
        return userEntityPageDto;
    }

    public void setUserEntityPageDto(UserEntityPageDto userEntityPageDto) {
        this.userEntityPageDto = userEntityPageDto;
    }
}
