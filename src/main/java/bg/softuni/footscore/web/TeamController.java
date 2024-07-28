package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.service.*;
import bg.softuni.footscore.utils.SeasonUtils;
import bg.softuni.footscore.utils.UserUtils;
import jakarta.transaction.Transactional;
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

    public TeamController(LeagueService leagueService,
                          SeasonService seasonService,
                          LeagueTeamSeasonService seasonLeagueTeamService,
                          TeamStatisticsService teamStatisticsService,
                          TeamService teamService,
                          UserService userService) {
        this.leagueService = leagueService;
        this.seasonService = seasonService;
        this.leagueTeamSeasonService = seasonLeagueTeamService;
        this.teamStatisticsService = teamStatisticsService;
        this.teamService = teamService;
        this.userService = userService;
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

        List<Season> seasons = this.seasonService.getAllSeasons();
        Set<Season> currentSeasons = SeasonUtils.getCurrentSeasonsForLeague(leagueId, leagueTeamSeasonService, seasons);

        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());

        model.addAttribute("league", leagueById);
        model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
        model.addAttribute("selectedSeasonId", seasonId);

        List<Team> allTeams = this.leagueTeamSeasonService.getAllTeamsBySeasonIdAndLeagueId(leagueId, seasonId);
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

        model.addAttribute("seasonId", this.seasonService.getSeasonById(seasonId).get());

        Optional<UserEntity> optionalUser = this.userService.getUser();

        if (optionalUser.isPresent()) {
            List<Team> allByIds  = new ArrayList<>();
            if (teamIds != null && !teamIds.isEmpty()) {
                allByIds = this.teamService.findAllByIds(teamIds);
            }

            if (!optionalUser.get().getFavoriteTeams().containsAll(allByIds)) {
                this.userService.addFavoriteTeams(optionalUser.get(), allByIds);
            }
        }

        return "redirect:/league/" + leagueId + "/teams";
    }

    @Transactional
    @GetMapping("/league/{leagueId}/teams/add-favorites")
    public String teamsAddFavorites(@PathVariable long leagueId,
                                    @RequestParam Long seasonId,
                                    Model model) {

        if (seasonId == null) {
            seasonId = this.seasonService.getAllSeasons().getLast().getId();
        }
        List<LeagueTeamSeason> byLeagueIdAndSeasonId = this.leagueTeamSeasonService.getByLeagueIdAndSeasonId(leagueId, seasonId);

        List<Team> teams = byLeagueIdAndSeasonId.stream().map(LeagueTeamSeason::getTeam).toList();

        Optional<UserEntity> optionalUser = this.userService.getUser();
        optionalUser.ifPresent(userEntity -> model.addAttribute("favoriteTeams", userEntity.getFavoriteTeams().stream().toList()));

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

        List<Season> seasons = this.seasonService.getAllSeasons();
        Set<Season> currentSeasons = SeasonUtils.getCurrentSeasonsForTeam(teamId, leagueTeamSeasonService, seasons);

        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());

        Optional<Team> teamOptional = this.teamService.findById(teamId);
        Optional<Season> seasonOptional = this.seasonService.getSeasonById(seasonId);

        if (teamOptional.isPresent() && seasonOptional.isPresent()) {
            List<LeagueTeamSeason> list = this.leagueTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);
            List<League> leagues = new ArrayList<>();

            for (LeagueTeamSeason leagueTeamSeason : list) {
                League league = leagueTeamSeason.getLeague();
                this.teamStatisticsService.saveApiStatistics(league.getApiId(), teamOptional.get().getApiId(), seasonOptional.get().getYear());
                leagues.add(league);
            }

            leagueId = getId(leagueId, leagues.getLast().getId());

            Optional<TeamStatistics> optional = this.teamStatisticsService.getByTeamIdAndSeasonYearAndLeagueId(teamId, seasonOptional.get().getYear(), leagueId);

            model.addAttribute("selectedSeasonId", seasonId);
            model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
            model.addAttribute("team", teamOptional.get());
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
