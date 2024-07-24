package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.TeamPageDto;
import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.repository.TeamRepository;
import bg.softuni.footscore.repository.VenueRepository;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonLeagueTeamService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.TeamService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final ModelMapper modelMapper;
    private final ApiConfig apiConfig;
    private final RestClient restClient;
    private final LeagueService leagueService;
    private final SeasonService seasonService;
    private final SeasonLeagueTeamService seasonLeagueTeamService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           ModelMapper modelMapper,
                           ApiConfig apiConfig,
                           RestClient restClient,
                           VenueRepository venueRepository,
                           LeagueService leagueService,
                           SeasonService seasonService,
                           SeasonLeagueTeamService seasonLeagueTeamService) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
        this.venueRepository = venueRepository;
        this.leagueService = leagueService;
        this.seasonService = seasonService;
        this.seasonLeagueTeamService = seasonLeagueTeamService;
    }


    @Override
    @Transactional
    public void saveApiTeamsForLeagueAndSeason(League league, Season season) {

        ResponseTeamApiDto response = this.getResponse(league.getApiId(), season.getYear());

        if (!response.getResponse().isEmpty()) {
            List<Team> teamsToSave = new ArrayList<>();
            List<Venue> venuesToSave = new ArrayList<>();

            response.getResponse().forEach(dto -> {
                if (this.getTeamByApiId(dto.getTeam().getId()).isEmpty()) {
                    Team team = this.modelMapper.map(dto.getTeam(), Team.class);
                    Venue venue = this.modelMapper.map(dto.getVenue(), Venue.class);
                    team.setVenue(venue);
                    team.setApiId(dto.getTeam().getId());

                    teamsToSave.add(team);
                    if (!venuesToSave.contains(venue)) {
                        venuesToSave.add(venue);
                    }
                }
            });

            this.venueRepository.saveAll(venuesToSave);

            this.teamRepository.saveAll(teamsToSave);

            response.getResponse().forEach(dto -> {
                Optional<Team> team = this.teamRepository.findByApiId(dto.getTeam().getId());
                if (team.isPresent()) {
                    Optional<Team> optionalTeam = this.seasonLeagueTeamService.getTeamByLeagueIdAndSeasonIdAndTeamId(league.getId(), season.getId(), team.get().getId());
                    if (optionalTeam.isEmpty()) {
                        SeasonLeagueTeam seasonLeagueTeam = new SeasonLeagueTeam();
                        seasonLeagueTeam.setSeason(season);
                        seasonLeagueTeam.setLeague(league);
                        seasonLeagueTeam.setTeam(team.get());

                        this.seasonLeagueTeamService.save(seasonLeagueTeam);
                    }
                }
            });
        }
    }


    @Override
    public ResponseTeamApiDto getResponse(long leagueApiId, int seasonYear) {
        String url = this.apiConfig.getUrl() + "teams?league=" + leagueApiId + "&season=" + seasonYear;

        return this.restClient
                .get()
                .uri(url)
                .header("x-rapidapi-key", this.apiConfig.getKey())
                .header("x-rapidapi-host", this.apiConfig.getUrl())
                .retrieve()
                .body(ResponseTeamApiDto.class);
    }

    @Override
    public boolean isEmpty() {
        return this.teamRepository.count() == 0;
    }

    @Override
    public List<TeamPageDto> findAllByIds(List<Long> ids) {
        return this.teamRepository.findAllById(ids).stream()
                .map(team -> this.modelMapper.map(team, TeamPageDto.class))
                .toList();
    }

    @Override
    public Optional<Team> findById(long teamId) {
        return this.teamRepository.findById(teamId);
    }

    @Override
    public Optional<Team> getTeamByApiId(long apiId) {
        return this.teamRepository.findByApiId(apiId);
    }

    @Override
    public Optional<Team> getTeamById(long teamId) {
        return this.teamRepository.findById(teamId);
    }
}
