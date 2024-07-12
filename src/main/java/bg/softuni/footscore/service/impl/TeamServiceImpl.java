package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.Venue;
import bg.softuni.footscore.repository.TeamRepository;
import bg.softuni.footscore.repository.VenueRepository;
import bg.softuni.footscore.service.LeagueService;
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

    public TeamServiceImpl(TeamRepository teamRepository,
                           ModelMapper modelMapper,
                           ApiConfig apiConfig,
                           RestClient restClient,
                           VenueRepository venueRepository, LeagueService leagueService, SeasonService seasonService) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
        this.venueRepository = venueRepository;
        this.leagueService = leagueService;
        this.seasonService = seasonService;
    }


    @Override
    @Transactional
    public void saveApiTeamsForLeagueAndSeason(long id) {
        Optional<League> optionalLeague = this.leagueService.getLeagueById(id);

        optionalLeague.ifPresent(league -> {
            int latestSeasonYear = this.seasonService.getAllSeasons().getLast().getYear();
            ResponseTeamApiDto response = this.getResponse(league.getApiId(), latestSeasonYear);

            if (!response.getResponse().isEmpty()) {
                List<Team> teamsToSave = new ArrayList<>();
                List<Venue> venuesToSave = new ArrayList<>();

                response.getResponse().forEach(r -> {
                    if (this.findByName(r.getTeam().getName()) == null) {
                        Team team = this.modelMapper.map(r.getTeam(), Team.class);
                        Venue venue = this.modelMapper.map(r.getVenue(), Venue.class);
                        team.setVenue(venue);
                        team.setLeague(league);
                        team.setApiId(r.getTeam().getId());
                        league.getTeams().add(team);

                        venuesToSave.add(venue);
                        teamsToSave.add(team);
                    }
                });

                if (!venuesToSave.isEmpty()) {
                    this.venueRepository.saveAll(venuesToSave);
                }
                if (!teamsToSave.isEmpty()) {
                    this.teamRepository.saveAll(teamsToSave);
                }
            }
        });
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
    public Team findByName(String name) {
        return this.teamRepository.findByName(name);
    }

    @Override
    public boolean isEmpty() {
        return this.teamRepository.count() == 0;
    }
}
