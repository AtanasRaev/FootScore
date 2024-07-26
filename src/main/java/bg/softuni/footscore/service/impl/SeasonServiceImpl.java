package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ResponseCountryLeagueSeasonsApiDto;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.repository.SeasonRepository;
import bg.softuni.footscore.service.SeasonService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SeasonServiceImpl implements SeasonService {
    private final SeasonRepository seasonRepository;
    private final ApiConfig apiConfig;
    private final RestClient restClient;
    private final ModelMapper modelMapper;

    public SeasonServiceImpl(SeasonRepository seasonRepository, ApiConfig apiConfig, RestClient restClient, ModelMapper modelMapper) {
        this.seasonRepository = seasonRepository;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
        this.modelMapper = modelMapper;
    }


    @Override
    public void saveApiSeasons(String name) {
        ResponseCountryLeagueSeasonsApiDto response = this.getResponse(name);

        List<Season> seasonsList = new ArrayList<>();

        response.getResponse()
                .getFirst()
                .getSeasons()
                .forEach(s -> seasonsList.add(this.modelMapper.map(s, Season.class)));

        this.seasonRepository.saveAll(seasonsList);
    }

    @Override
    public List<Season> getAllSeasons() {
        return this.seasonRepository.findAll();
    }

    @Override
    public boolean isEmpty() {
        return this.seasonRepository.count() == 0;
    }

    @Override
    public ResponseCountryLeagueSeasonsApiDto getResponse(String name) {
        String url = this.apiConfig.getUrl() + "leagues?country=" + name;

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(ResponseCountryLeagueSeasonsApiDto.class);
    }

    @Override
    public Optional<Season> getSeasonByYear(int seasonYear) {
        return this.seasonRepository.findByYear(seasonYear);
    }

    @Override
    public Optional<Season> getSeasonById(long id) {
        return this.seasonRepository.findById(id);
    }
}
