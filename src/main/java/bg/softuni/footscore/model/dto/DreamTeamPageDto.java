package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;

import java.util.List;

public class DreamTeamPageDto {
    private Long id;
    private String name;
    private String formation;
    private List<PlayerPageDto> players;
    private UserEntityPageDto user;

    public DreamTeamPageDto() {
    }

    public DreamTeamPageDto(String name, String formation, List<PlayerPageDto> players, UserEntityPageDto user) {
        this.name = name;
        this.formation = formation;
        this.players = players;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserEntityPageDto getUser() {
        return user;
    }

    public void setUser(UserEntityPageDto user) {
        this.user = user;
    }
}
