package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.dto.teamDto.VenuePageDto;
import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.repository.TeamRepository;
import bg.softuni.footscore.service.LeagueTeamSeasonService;
import bg.softuni.footscore.service.TeamService;
import bg.softuni.footscore.service.VenueService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final VenueService venueService;
    private final ModelMapper modelMapper;
    private final ApiConfig apiConfig;
    private final RestClient restClient;
    private final LeagueTeamSeasonService leagueTeamSeasonService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           VenueService venueService,
                           ModelMapper modelMapper,
                           ApiConfig apiConfig,
                           @Qualifier("genericRestClient") RestClient restClient,
                           LeagueTeamSeasonService seasonLeagueTeamService) {
        this.teamRepository = teamRepository;
        this.venueService = venueService;
        this.modelMapper = modelMapper;
        this.apiConfig = apiConfig;
        this.restClient = restClient;
        this.leagueTeamSeasonService = seasonLeagueTeamService;
    }


    @Override
    @Transactional
    public void saveApiTeamsForLeagueAndSeason(LeaguePageDto league, SeasonPageDto season) {

        if (league == null || season == null) {
            throw new InvalidParameterException("League and Season are required");
        }

        ResponseTeamApiDto response = this.getResponse(league.getApiId(), season.getYear());

        if (!response.getResponse().isEmpty()) {
            List<TeamPageDto> teamsToSave = new ArrayList<>();
            List<VenuePageDto> venuesToSave = new ArrayList<>();

            response.getResponse().forEach(dto -> {
                if (this.getTeamByApiId(dto.getTeam().getId()) == null) {
                    VenuePageDto venue = new VenuePageDto(dto.getVenue().getName(), dto.getVenue().getCity(), dto.getVenue().getCapacity());
                    TeamPageDto team = new TeamPageDto(dto.getTeam().getName(), dto.getTeam().getLogo(), venue, dto.getTeam().getId());

                    TeamPageDto teamByApiId = this.getTeamByApiId(team.getApiId());

                    if (teamByApiId == null) {
                        teamsToSave.add(team);
                        if (!venuesToSave.contains(venue)) {
                            venuesToSave.add(venue);
                        }
                    }
                }
            });

            if (!venuesToSave.isEmpty() && !teamsToSave.isEmpty()) {
                this.venueService.saveAll(venuesToSave);
                this.saveAll(teamsToSave);
            }

            response.getResponse().forEach(dto -> {
                Optional<Team> team = this.teamRepository.findByApiId(dto.getTeam().getId());
                if (team.isPresent()) {
                    List<LeagueTeamSeasonPageDto> byLeagueIdAndSeasonId = this.leagueTeamSeasonService.getByLeagueIdAndSeasonId(league.getId(), season.getId());
                    List<Team> teams = byLeagueIdAndSeasonId.stream().map(s -> this.modelMapper.map(s.getTeam(), Team.class)).toList();

                    if (teams.isEmpty() || !teams.contains(team.get())) {
                        LeagueTeamSeason seasonLeagueTeam = new LeagueTeamSeason();
                        seasonLeagueTeam.setSeason(this.modelMapper.map(season, Season.class));
                        seasonLeagueTeam.setLeague(this.modelMapper.map(league, League.class));
                        seasonLeagueTeam.setTeam(team.get());

                        this.leagueTeamSeasonService.save(seasonLeagueTeam);
                    }
                }
            });
        }
    }


    @Override
    public ResponseTeamApiDto getResponse(Long leagueApiId, Integer seasonYear) {
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
        return this.teamRepository.findAllById(ids).stream().map(t -> this.modelMapper.map(t, TeamPageDto.class)).toList();
    }

    @Override
    public TeamPageDto findById(Long teamId) {
        return this.teamRepository.findById(teamId)
                .map(t -> this.modelMapper.map(t, TeamPageDto.class))
                .orElse(null);
    }

    @Override
    public TeamPageDto getTeamByApiId(Long apiId) {
        return this.teamRepository.findByApiId(apiId)
                .map(t -> this.modelMapper.map(t, TeamPageDto.class))
                .orElse(null);
    }

    @Override
    public TeamPageDto getTeamById(Long teamId) {
        return this.teamRepository.findById(teamId)
                .map(t -> this.modelMapper.map(t, TeamPageDto.class))
                .orElse(null);
    }

    @Override
    public void updateTeam(TeamPageDto team) {
        Team map = this.modelMapper.map(team, Team.class);
        map.setVenue(this.modelMapper.map(team.getVenue(), Venue.class));
        this.teamRepository.save(map);
    }

    @Override
    @Transactional
    public void fetchTeams(List<LeaguePageDto> leagues, List<SeasonPageDto> seasons) {
        if (leagues == null || leagues.isEmpty() || seasons == null || seasons.isEmpty()) {
            throw new EntityNotFoundException("Not found leagues or seasons");
        }
        seasons.forEach(season -> {
            leagues.forEach(league -> {
                List<LeagueTeamSeasonPageDto> list = this.leagueTeamSeasonService.getByLeagueIdAndSeasonId(league.getId(), season.getId());
                if (list.isEmpty()) {
                    saveApiTeamsForLeagueAndSeason(league, season);
                }
            });
        });
    }

    @Override
    public void saveAll(List<TeamPageDto> teamsToSave) {
        this.teamRepository.saveAll(teamsToSave.stream().map(t -> this.modelMapper.map(t, Team.class)).toList());
    }
}
