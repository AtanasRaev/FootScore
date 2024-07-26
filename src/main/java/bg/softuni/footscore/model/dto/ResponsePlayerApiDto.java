package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.model.dto.playerDto.PageApiDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerStatisticsApiDto;

import java.util.List;

public class ResponsePlayerApiDto {
    private PageApiDto paging;
    private List<PlayerStatisticsApiDto> response;

    public ResponsePlayerApiDto() {
    }

    public PageApiDto getPaging() {
        return paging;
    }

    public void setPaging(PageApiDto paging) {
        this.paging = paging;
    }

    public List<PlayerStatisticsApiDto> getResponse() {
        return response;
    }

    public void setResponse(List<PlayerStatisticsApiDto> response) {
        this.response = response;
    }
}
