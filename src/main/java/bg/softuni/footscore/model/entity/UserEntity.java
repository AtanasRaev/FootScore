package bg.softuni.footscore.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;


    @OneToMany
    private List<Team> favouriteTeams;

    @OneToMany
    private List<Player> favouritePlayers;

    public UserEntity() {
        this.favouriteTeams = new ArrayList<>();
        this.favouritePlayers = new ArrayList<>();
    }

    public List<Team> getFavouriteTeams() {
        return favouriteTeams;
    }

    public void setFavouriteTeams(List<Team> favouriteTeams) {
        this.favouriteTeams = favouriteTeams;
    }

    public List<Player> getFavouritePlayers() {
        return favouritePlayers;
    }

    public void setFavouritePlayers(List<Player> favouritePlayers) {
        this.favouritePlayers = favouritePlayers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
