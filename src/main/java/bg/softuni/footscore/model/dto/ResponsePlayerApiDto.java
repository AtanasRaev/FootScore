package bg.softuni.footscore.model.dto;

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
