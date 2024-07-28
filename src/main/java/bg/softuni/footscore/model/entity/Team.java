package bg.softuni.footscore.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String logo;

    @OneToOne(cascade = CascadeType.ALL)
    private Venue venue;

    @Column
    private Long apiId;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<TeamStatistics> statistics;

    public Team() {
        this.statistics = new ArrayList<>();
    }

    public Team(String name, String logo, Venue venue, Long apiId) {
        this.name = name;
        this.logo = logo;
        this.venue = venue;
        this.apiId = apiId;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<TeamStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<TeamStatistics> statistics) {
        this.statistics = statistics;
    }
}
