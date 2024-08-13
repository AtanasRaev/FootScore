package bg.softuni.footscore.model.dto.teamDto;

public class AverageDto {
    private String home;
    private String away;
    private String total;

    public AverageDto(String home, String away, String total) {
        this.home = home;
        this.away = away;
        this.total = total;
    }

    public AverageDto() {
    }

    public String getHome() { return home; }
    public void setHome(String home) { this.home = home; }

    public String getAway() { return away; }
    public void setAway(String away) { this.away = away; }

    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }
}
