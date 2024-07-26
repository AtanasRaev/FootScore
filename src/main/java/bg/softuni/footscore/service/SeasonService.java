package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.entity.Season;

import java.util.List;
import java.util.Optional;

public interface SeasonService {
    void saveApiSeasons(String name);

    List<Season> getAllSeasons();

    boolean isEmpty();

    ResponseCountryLeagueSeasonsApiDto getResponse(String name);

    Optional<Season> getSeasonByYear(int seasonYear);

    Optional<Season> getSeasonById(long id);
}
