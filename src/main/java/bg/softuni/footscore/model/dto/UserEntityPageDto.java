package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;

import java.util.Set;

public class UserEntityPageDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private RolePageDto role;
    private Set<TeamPageDto> favoriteTeams;
    private Set<PlayerPageDto> favoritePlayers;

    public UserEntityPageDto() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RolePageDto getRole() {
        return role;
    }

    public void setRole(RolePageDto role) {
        this.role = role;
    }

    public Set<TeamPageDto> getFavoriteTeams() {
        return favoriteTeams;
    }

    public void setFavoriteTeams(Set<TeamPageDto> favoriteTeams) {
        this.favoriteTeams = favoriteTeams;
    }

    public Set<PlayerPageDto> getFavoritePlayers() {
        return favoritePlayers;
    }

    public void setFavoritePlayers(Set<PlayerPageDto> favoritePlayers) {
        this.favoritePlayers = favoritePlayers;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
