package bg.softuni.footscore.model.dto.teamDto;

public class PenaltyDto {
    private PenaltyDetailDto scored;
    private PenaltyDetailDto missed;
    private Integer total;

    public PenaltyDetailDto getScored() { return scored; }
    public void setScored(PenaltyDetailDto scored) { this.scored = scored; }

    public PenaltyDetailDto getMissed() { return missed; }
    public void setMissed(PenaltyDetailDto missed) { this.missed = missed; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
