package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.config.ApiConfig;
import bg.softuni.footscore.model.dto.ResponseTeamApiDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.repository.TeamRepository;
import bg.softuni.footscore.repository.VenueRepository;
import bg.softuni.footscore.service.LeagueTeamSeasonService;
import bg.softuni.footscore.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
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
    public void saveApiTeamsForLeagueAndSeason(LeaguePageDto league, SeasonPageDto season) {

        ResponseTeamApiDto response = this.getResponse(league.getApiId(), season.getYear());

        if (!response.getResponse().isEmpty()) {
            List<Team> teamsToSave = new ArrayList<>();
            List<Venue> venuesToSave = new ArrayList<>();

            response.getResponse().forEach(dto -> {
                if (this.getTeamByApiId(dto.getTeam().getId()).isEmpty()) {
                    Venue venue = new Venue(dto.getVenue().getName(), dto.getVenue().getCity(), dto.getVenue().getCapacity());
                    Team team = new Team(dto.getTeam().getName(), dto.getTeam().getLogo(), venue, dto.getTeam().getId());

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
                    List<LeagueTeamSeason> byLeagueIdAndSeasonId = this.leagueTeamSeasonService.getByLeagueIdAndSeasonId(league.getId(), season.getId());
                    List<Team> teams = byLeagueIdAndSeasonId.stream().map(LeagueTeamSeason::getTeam).toList();

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
    public List<Team> findAllByIds(List<Long> ids) {
        return this.teamRepository.findAllById(ids);
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
    public void updateTeam(Team team) {
        this.teamRepository.save(team);
    }

    @Override
    @Transactional
    public void fetchTeams(List<LeaguePageDto> leagues, List<SeasonPageDto> seasons) {
        if (leagues == null || leagues.isEmpty() || seasons == null || seasons.isEmpty()) {
            throw new EntityNotFoundException("Not found leagues or seasons");
        }
        seasons.forEach(season -> {
            leagues.forEach(league -> {
                List<LeagueTeamSeason> list = this.leagueTeamSeasonService.getByLeagueIdAndSeasonId(league.getId(), season.getId());
                if (list.isEmpty()) {
                    saveApiTeamsForLeagueAndSeason(league, season);
                }
            });
        });
    }
}
