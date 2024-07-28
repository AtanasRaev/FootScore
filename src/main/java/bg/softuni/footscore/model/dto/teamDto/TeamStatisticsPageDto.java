package bg.softuni.footscore.model.dto.teamDto;

import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;

public class TeamStatisticsPageDto {
    private Long id;
    private LeaguePageDto league;
    private SeasonPageDto season;
    private TeamPageDto team;
    private String form;
    private Integer fixturesPlayedHome;
    private Integer fixturesPlayedAway;
    private Integer fixturesPlayedTotal;
    private Integer fixturesWinsHome;
    private Integer fixturesWinsAway;
    private Integer fixturesWinsTotal;
    private Integer fixturesDrawsHome;
    private Integer fixturesDrawsAway;
    private Integer fixturesDrawsTotal;
    private Integer fixturesLosesHome;
    private Integer fixturesLosesAway;
    private Integer fixturesLosesTotal;
    private Integer goalsForHome;
    private Integer goalsForAway;
    private Integer goalsForTotal;
    private Integer goalsAgainstTotal;
    private Integer goalDifference;
    private Integer cleanSheetsTotal;
    private Integer penaltyScoredTotal;
    private Integer penaltyMissedTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaguePageDto getLeague() {
        return league;
    }

    public void setLeague(LeaguePageDto league) {
        this.league = league;
    }

    public SeasonPageDto getSeason() {
        return season;
    }

    public void setSeason(SeasonPageDto season) {
        this.season = season;
    }

    public TeamPageDto getTeam() {
        return team;
    }

    public void setTeam(TeamPageDto team) {
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

    public Integer getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(Integer goalDifference) {
        this.goalDifference = goalDifference;
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
}
