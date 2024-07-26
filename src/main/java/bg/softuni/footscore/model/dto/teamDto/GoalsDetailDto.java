package bg.softuni.footscore.model.dto.teamDto;

import java.util.Map;

public class GoalsDetailDto {
    private TotalDto total;
    private AverageDto average;
    private Map<String, MinuteDto> minute;

    public TotalDto getTotal() { return total; }
    public void setTotal(TotalDto total) { this.total = total; }

    public AverageDto getAverage() { return average; }
    public void setAverage(AverageDto average) { this.average = average; }

    public Map<String, MinuteDto> getMinute() { return minute; }
    public void setMinute(Map<String, MinuteDto> minute) { this.minute = minute; }
}
