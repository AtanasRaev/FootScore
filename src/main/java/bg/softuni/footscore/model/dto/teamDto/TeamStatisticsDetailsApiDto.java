package bg.softuni.footscore.model.dto.teamDto;

import bg.softuni.footscore.model.dto.leagueDto.LeagueApiDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TeamStatisticsDetailsApiDto {
    private LeagueApiDto league;
    private TeamApiDto team;
    private String form;
    private FixturesDto fixtures;
    private GoalsDto goals;
    @JsonProperty("clean_sheet")
    private CleanSheetDto cleanSheet;
    private PenaltyDto penalty;
    private List<LineupDto> lineups;

    public LeagueApiDto getLeague() {
        return league;
    }

    public void setLeague(LeagueApiDto league) {
        this.league = league;
    }

    public TeamApiDto getTeam() {
        return team;
    }

    public void setTeam(TeamApiDto team) {
        this.team = team;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public FixturesDto getFixtures() {
        return fixtures;
    }

    public void setFixtures(FixturesDto fixtures) {
        this.fixtures = fixtures;
    }

    public GoalsDto getGoals() {
        return goals;
    }

    public void setGoals(GoalsDto goals) {
        this.goals = goals;
    }

    public CleanSheetDto getCleanSheet() {
        return cleanSheet;
    }

    public void setCleanSheet(CleanSheetDto cleanSheet) {
        this.cleanSheet = cleanSheet;
    }

    public PenaltyDto getPenalty() {
        return penalty;
    }

    public void setPenalty(PenaltyDto penalty) {
        this.penalty = penalty;
    }

    public List<LineupDto> getLineups() {
        return lineups;
    }

    public void setLineups(List<LineupDto> lineups) {
        this.lineups = lineups;
    }
}
