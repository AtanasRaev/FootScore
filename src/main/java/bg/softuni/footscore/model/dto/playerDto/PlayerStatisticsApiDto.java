package bg.softuni.footscore.model.dto.playerDto;

import java.util.List;

public class PlayerStatisticsApiDto {
    private PlayerApiDto player;

    private List<StatisticsApiDto> statistics;

    public PlayerStatisticsApiDto() {
    }

    public PlayerStatisticsApiDto(PlayerApiDto player, List<StatisticsApiDto> statistics) {
        this.player = player;
        this.statistics = statistics;
    }


    public PlayerApiDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerApiDto player) {
        this.player = player;
    }

    public List<StatisticsApiDto> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<StatisticsApiDto> statistics) {
        this.statistics = statistics;
    }
}
