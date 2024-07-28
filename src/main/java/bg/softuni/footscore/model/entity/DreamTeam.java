package bg.softuni.footscore.model.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "dream_teams")
public class DreamTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String formation;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Player> players;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    public DreamTeam() {}

    public DreamTeam(String name,
                     String formation,
                     Set<Player> players,
                     UserEntity user) {
        this.name = name;
        this.formation = formation;
        this.players = players;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DreamTeam dreamTeam = (DreamTeam) o;
        return id == dreamTeam.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DreamTeam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", formation='" + formation + '\'' +
                ", players=" + players +
                ", user=" + user +
                '}';
    }
}
