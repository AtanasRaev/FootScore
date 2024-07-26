package bg.softuni.footscore.model.entity;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "team_statistics")
public class TeamStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private League league;

   @ManyToOne
   private Season season;

    @ManyToOne
    private Team team;

    @Column(name = "form")
    private String form;

    @Column(name = "fixtures_played_home")
    private Integer fixturesPlayedHome;

    @Column(name = "fixtures_played_away")
    private Integer fixturesPlayedAway;

    @Column(name = "fixtures_played_total")
    private Integer fixturesPlayedTotal;

    @Column(name = "fixtures_wins_home")
    private Integer fixturesWinsHome;

    @Column(name = "fixtures_wins_away")
    private Integer fixturesWinsAway;

    @Column(name = "fixtures_wins_total")
    private Integer fixturesWinsTotal;

    @Column(name = "fixtures_draws_home")
    private Integer fixturesDrawsHome;

    @Column(name = "fixtures_draws_away")
    private Integer fixturesDrawsAway;

    @Column(name = "fixtures_draws_total")
    private Integer fixturesDrawsTotal;

    @Column(name = "fixtures_loses_home")
    private Integer fixturesLosesHome;

    @Column(name = "fixtures_loses_away")
    private Integer fixturesLosesAway;

    @Column(name = "fixtures_loses_total")
    private Integer fixturesLosesTotal;

    @Column(name = "goals_for_home")
    private Integer goalsForHome;

    @Column(name = "goals_for_away")
    private Integer goalsForAway;

    @Column(name = "goals_for_total")
    private Integer goalsForTotal;

    @Column(name = "goals_against_total")
    private Integer goalsAgainstTotal;

    @Column(name = "goal_difference")
    private Integer goalDifference;

    @Column(name = "clean_sheets_total")
    private Integer cleanSheetsTotal;


    @Column(name = "penalty_scored_total")
    private Integer penaltyScoredTotal;

    @Column(name = "penalty_missed_total")
    private Integer penaltyMissedTotal;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lineups", joinColumns = @JoinColumn(name = "team_statistics_id"))
    @MapKeyColumn(name = "formation")
    @Column(name = "played")
    private Map<String, Integer> lineups;


    public TeamStatistics() {
        this.lineups = new HashMap<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Integer getFixturesPlayedHome() {
        return fixturesPlayedHome;
    }

    public void setFixturesPlayedHome(Integer fixturesPlayedHome) {
        this.fixturesPlayedHome = fixturesPlayedHome;
    }

    public Integer getFixturesPlayedAway() {
        return fixturesPlayedAway;
    }

    public void setFixturesPlayedAway(Integer fixturesPlayedAway) {
        this.fixturesPlayedAway = fixturesPlayedAway;
    }

    public Integer getFixturesPlayedTotal() {
        return fixturesPlayedTotal;
    }

    public void setFixturesPlayedTotal(Integer fixturesPlayedTotal) {
        this.fixturesPlayedTotal = fixturesPlayedTotal;
    }

    public Integer getFixturesWinsHome() {
        return fixturesWinsHome;
    }

    public void setFixturesWinsHome(Integer fixturesWinsHome) {
        this.fixturesWinsHome = fixturesWinsHome;
    }

    public Integer getFixturesWinsAway() {
        return fixturesWinsAway;
    }

    public void setFixturesWinsAway(Integer fixturesWinsAway) {
        this.fixturesWinsAway = fixturesWinsAway;
    }

    public Integer getFixturesWinsTotal() {
        return fixturesWinsTotal;
    }

    public Integer getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(Integer goalDifference) {
        this.goalDifference = goalDifference;
    }

    public void setFixturesWinsTotal(Integer fixturesWinsTotal) {
        this.fixturesWinsTotal = fixturesWinsTotal;
    }

    public Integer getFixturesDrawsHome() {
        return fixturesDrawsHome;
    }

    public void setFixturesDrawsHome(Integer fixturesDrawsHome) {
        this.fixturesDrawsHome = fixturesDrawsHome;
    }

    public Integer getFixturesDrawsAway() {
        return fixturesDrawsAway;
    }

    public void setFixturesDrawsAway(Integer fixturesDrawsAway) {
        this.fixturesDrawsAway = fixturesDrawsAway;
    }

    public Integer getFixturesDrawsTotal() {
        return fixturesDrawsTotal;
    }

    public void setFixturesDrawsTotal(Integer fixturesDrawsTotal) {
        this.fixturesDrawsTotal = fixturesDrawsTotal;
    }

    public Integer getFixturesLosesHome() {
        return fixturesLosesHome;
    }

    public void setFixturesLosesHome(Integer fixturesLosesHome) {
        this.fixturesLosesHome = fixturesLosesHome;
    }

    public Integer getFixturesLosesAway() {
        return fixturesLosesAway;
    }

    public void setFixturesLosesAway(Integer fixturesLosesAway) {
        this.fixturesLosesAway = fixturesLosesAway;
    }

    public Integer getFixturesLosesTotal() {
        return fixturesLosesTotal;
    }

    public void setFixturesLosesTotal(Integer fixturesLosesTotal) {
        this.fixturesLosesTotal = fixturesLosesTotal;
    }

    public Integer getGoalsForHome() {
        return goalsForHome;
    }

    public void setGoalsForHome(Integer goalsForHome) {
        this.goalsForHome = goalsForHome;
    }

    public Integer getGoalsForAway() {
        return goalsForAway;
    }

    public void setGoalsForAway(Integer goalsForAway) {
        this.goalsForAway = goalsForAway;
    }

    public Integer getGoalsForTotal() {
        return goalsForTotal;
    }

    public void setGoalsForTotal(Integer goalsForTotal) {
        this.goalsForTotal = goalsForTotal;
    }

    public Integer getGoalsAgainstTotal() {
        return goalsAgainstTotal;
    }

    public void setGoalsAgainstTotal(Integer goalsAgainstTotal) {
        this.goalsAgainstTotal = goalsAgainstTotal;
    }

    public Integer getCleanSheetsTotal() {
        return cleanSheetsTotal;
    }

    public void setCleanSheetsTotal(Integer cleanSheetsTotal) {
        this.cleanSheetsTotal = cleanSheetsTotal;
    }

    public Integer getPenaltyScoredTotal() {
        return penaltyScoredTotal;
    }

    public void setPenaltyScoredTotal(Integer penaltyScoredTotal) {
        this.penaltyScoredTotal = penaltyScoredTotal;
    }

    public Integer getPenaltyMissedTotal() {
        return penaltyMissedTotal;
    }

    public void setPenaltyMissedTotal(Integer penaltyMissedTotal) {
        this.penaltyMissedTotal = penaltyMissedTotal;
    }

    public Map<String, Integer> getLineups() {
        return lineups;
    }

    public void setLineups(Map<String, Integer> lineups) {
        this.lineups = lineups;
    }
}
