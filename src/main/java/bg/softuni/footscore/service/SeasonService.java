package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;

import java.util.List;

public interface SeasonService {
    void saveApiSeasons(String name);

    List<SeasonPageDto> getAllSeasons();

    boolean isEmpty();

    ResponseCountryLeagueSeasonsApiDto getResponse(String name);

    SeasonPageDto getSeasonByYear(Integer seasonYear);

    SeasonPageDto getSeasonById(Long id);
}
