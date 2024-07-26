package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.repository.TeamRepository;
import bg.softuni.footscore.repository.VenueRepository;
import bg.softuni.footscore.service.LeagueTeamSeasonService;
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
    private final LeagueTeamSeasonService leagueTeamSeasonService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           ModelMapper modelMapper,
                           ApiConfig apiConfig,
                           RestClient restClient,
                           VenueRepository venueRepository,
                           LeagueTeamSeasonService seasonLeagueTeamService) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
        this.venueRepository = venueRepository;
        this.leagueTeamSeasonService = seasonLeagueTeamService;
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

                    Optional<Team> optional = this.teamRepository.findByApiId(team.getApiId());

                    if (optional.isEmpty()) {
                        teamsToSave.add(team);
                        if (!venuesToSave.contains(venue)) {
                            venuesToSave.add(venue);
                        }
                    }
                }
            });

            teamsToSave.forEach(team -> {
                Optional<Team> optional = this.teamRepository.findByApiId(team.getApiId());

                optional.ifPresent(teamsToSave::remove);
            });

            if (!venuesToSave.isEmpty() && !teamsToSave.isEmpty()) {
                this.venueRepository.saveAll(venuesToSave);
                this.teamRepository.saveAll(teamsToSave);
            }

            response.getResponse().forEach(dto -> {
                Optional<Team> team = this.teamRepository.findByApiId(dto.getTeam().getId());
                if (team.isPresent()) {
                    Optional<Team> optionalTeam = this.leagueTeamSeasonService.getTeamByLeagueIdAndSeasonIdAndTeamId(league.getId(), season.getId(), team.get().getId());
                    if (optionalTeam.isEmpty()) {
                        LeagueTeamSeason seasonLeagueTeam = new LeagueTeamSeason();
                        seasonLeagueTeam.setSeason(season);
                        seasonLeagueTeam.setLeague(league);
                        seasonLeagueTeam.setTeam(team.get());

                        this.leagueTeamSeasonService.save(seasonLeagueTeam);
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

    @Override
    public void update(Team team) {
        this.teamRepository.save(team);
    }
}
