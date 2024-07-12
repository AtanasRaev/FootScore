package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.entity.Season;

import java.util.List;

public interface SeasonService {
    void saveApiSeasons(String name);

    List<Season> getAllSeasons();

    boolean isEmpty();

    ResponseCountryLeagueSeasonsApiDto getResponse(String name);

    Season getSeasonByYear(int seasonYear);
}
