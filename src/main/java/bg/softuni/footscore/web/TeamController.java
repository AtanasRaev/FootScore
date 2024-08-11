package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.service.*;
import bg.softuni.footscore.utils.SeasonUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class TeamController {
    private final LeagueService leagueService;
    private final SeasonService seasonService;
    private final LeagueTeamSeasonService leagueTeamSeasonService;
    private final TeamStatisticsService teamStatisticsService;
    private final TeamService teamService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public TeamController(LeagueService leagueService,
                          SeasonService seasonService,
                          LeagueTeamSeasonService seasonLeagueTeamService,
                          TeamStatisticsService teamStatisticsService,
                          TeamService teamService,
                          UserService userService,
                          ModelMapper modelMapper) {
        this.leagueService = leagueService;
        this.seasonService = seasonService;
        this.leagueTeamSeasonService = seasonLeagueTeamService;
        this.teamStatisticsService = teamStatisticsService;
        this.teamService = teamService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/league/{leagueId}/teams")
    public String teams(@PathVariable Long leagueId,
                        @RequestParam(required = false) Long seasonId,
                        Model model) {
        if (leagueId == null) {
            return "redirect:/league-error";
        }
        LeaguePageDto leagueById = this.leagueService.getLeagueById(leagueId);

        if (leagueById == null) {
            return "redirect:/league-error";
        }

        List<SeasonPageDto> seasons = this.seasonService.getAllSeasons();
        Set<SeasonPageDto> currentSeasons = SeasonUtils.getCurrentSeasonsForLeague(leagueId, leagueTeamSeasonService, seasons);

        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());

        model.addAttribute("league", leagueById);
        model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
        model.addAttribute("selectedSeasonId", seasonId);

        List<TeamPageDto> allTeams = this.leagueTeamSeasonService.getAllByLeagueIdAndSeasonId(leagueId, seasonId).stream().map(LeagueTeamSeasonPageDto::getTeam).toList();

        model.addAttribute("teams", allTeams);

        return "teams";
    }

    @Transactional
    @PostMapping("/league/{leagueId}/teams")
    public String addTeam(@PathVariable Long leagueId,
                          @RequestParam(required = false) Long seasonId,
                          @RequestParam(required = false) List<Long> teamIds,
                          Model model) {

        if (leagueId == null) {
            return "redirect:/league-error";
        }

        if (seasonId == null) {
            seasonId = this.seasonService.getAllSeasons().getLast().getId();
        }

        model.addAttribute("seasonId", this.seasonService.getSeasonById(seasonId));

        UserEntityPageDto user = this.userService.getUser();

        if (user != null) {
            this.userService.addTeamsToFavorites(teamIds, user);
        }

        return "redirect:/league/" + leagueId + "/teams" + "?seasonId=" + seasonId;
    }


    @Transactional
    @GetMapping("/league/{leagueId}/teams/add-favorites")
    public String teamsAddFavorites(@PathVariable long leagueId,
                                    @RequestParam Long seasonId,
                                    Model model) {

        if (seasonId == null) {
            seasonId = this.seasonService.getAllSeasons().getLast().getId();
        }
        List<LeagueTeamSeasonPageDto> byLeagueIdAndSeasonId = this.leagueTeamSeasonService.getByLeagueIdAndSeasonId(leagueId, seasonId);
        List<TeamPageDto> teams = byLeagueIdAndSeasonId.stream().map(s -> this.modelMapper.map(s.getTeam(), TeamPageDto.class)).toList();

        UserEntityPageDto user = this.userService.getUser();

        if (user != null) {
            List<TeamPageDto> list = this.userService.getFavoriteTeams(teams, user);
            model.addAttribute("favoriteTeams", list);
        }

        model.addAttribute("teams", teams);
        model.addAttribute("league", byLeagueIdAndSeasonId.getFirst().getLeague());
        model.addAttribute("season", byLeagueIdAndSeasonId.getFirst().getSeason());

        return "add-favorites-teams";
    }

    @GetMapping("/team/{teamId}/details")
    public String teamDetails(@PathVariable long teamId,
                              @RequestParam(required = false) Long seasonId,
                              @RequestParam(required = false) Long leagueId,
                              Model model) {

        List<SeasonPageDto> seasons = this.seasonService.getAllSeasons();
        Set<SeasonPageDto> currentSeasons = SeasonUtils.getCurrentSeasonsForTeam(teamId, leagueTeamSeasonService, seasons);

        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());

        TeamPageDto teamOptional = this.teamService.findById(teamId);
        SeasonPageDto seasonOptional = this.seasonService.getSeasonById(seasonId);

        if (teamOptional != null&& seasonOptional != null) {
            List<LeagueTeamSeasonPageDto> list = this.leagueTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);
            List<LeaguePageDto> leagues = new ArrayList<>();

            for (LeagueTeamSeasonPageDto leagueTeamSeason : list) {
                LeaguePageDto league = leagueTeamSeason.getLeague();
                this.teamStatisticsService.saveApiStatistics(league.getApiId(), teamOptional.getApiId(), seasonOptional.getYear());
                leagues.add(league);
            }

            leagueId = getId(leagueId, leagues.getLast().getId());

            Optional<TeamStatistics> optional = this.teamStatisticsService.getByTeamIdAndSeasonYearAndLeagueId(teamId, seasonOptional.getYear(), leagueId);

            model.addAttribute("selectedSeasonId", seasonId);
            model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
            model.addAttribute("team", teamOptional);
            model.addAttribute("leagues", leagues);

            optional.ifPresent(statistics -> {
                model.addAttribute("statistics", statistics);
            });

        }
        return "team-details";
    }

    private static Long getId(Long seasonId, long currentSeasonId) {
        return seasonId != null ? seasonId : currentSeasonId;
    }
}
