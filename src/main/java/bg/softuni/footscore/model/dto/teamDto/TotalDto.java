package bg.softuni.footscore.model.dto.teamDto;

public class TotalDto {
    private Integer home;
    private Integer away;
    private Integer total;

    public TotalDto() {
    }

    public Integer getHome() { return home; }
    public void setHome(Integer home) { this.home = home; }

    public Integer getAway() { return away; }
    public void setAway(Integer away) { this.away = away; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
