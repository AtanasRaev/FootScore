package bg.softuni.footscore.model.dto.teamDto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GoalsDto {
    @JsonProperty("for")
    private GoalsDetailDto forGoals;

    @JsonProperty("against")
    private GoalsDetailDto against;

    public GoalsDto() {
    }

    public GoalsDetailDto getForGoals() {
        return forGoals;
    }

    public void setForGoals(GoalsDetailDto forGoals) {
        this.forGoals = forGoals;
    }

    public GoalsDetailDto getAgainst() {
        return against;
    }

    public void setAgainst(GoalsDetailDto against) {
        this.against = against;
    }
}