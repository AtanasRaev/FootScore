package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TeamController {
    private final LeagueService leagueService;
    private final SeasonService seasonService;
    private final LeagueTeamSeasonService leagueTeamSeasonService;
    private final TeamStatisticsService teamStatisticsService;
    private final TeamService teamService;

    public TeamController(LeagueService leagueService,
                          SeasonService seasonService,
                          LeagueTeamSeasonService seasonLeagueTeamService,
                          TeamStatisticsService teamStatisticsService,
                          TeamService teamService) {
        this.leagueService = leagueService;
        this.seasonService = seasonService;
        this.leagueTeamSeasonService = seasonLeagueTeamService;
        this.teamStatisticsService = teamStatisticsService;
        this.teamService = teamService;
    }

    @GetMapping("/league/{leagueId}/teams")
    public String teams(@PathVariable long leagueId,
                        @RequestParam(required = false) Long seasonId,
                        Model model) {
        Optional<League> leagueById = this.leagueService.getLeagueById(leagueId);
        List<Season> seasons = this.seasonService.getAllSeasons();

        if (leagueById.isPresent()) {
            model.addAttribute("league", leagueById.get());
            model.addAttribute("seasons", seasons.reversed());
            model.addAttribute("selectedSeasonId", seasonId);

            seasonId = this.getSeasonId(seasonId, seasons);

            List<Team> allTeams = this.leagueTeamSeasonService.getAllTeamsBySeasonIdAndLeagueId(leagueId, seasonId);

            model.addAttribute("teams", allTeams);
        }
        return "teams";
    }

    @GetMapping("/team/{teamId}/details")
    public String teamDetails(@PathVariable long teamId,
                              @RequestParam(required = false) Long seasonId,
                              Model model) {

        List<Season> seasons = this.seasonService.getAllSeasons();
        seasonId = this.getSeasonId(seasonId, seasons);

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

            Optional<TeamStatistics> optional = this.teamStatisticsService.getByTeamApiIdAndSeasonYearAndLeagueApiId(teamOptional.get().getApiId(), seasonOptional.get().getYear(), leagues.getFirst().getApiId());

            model.addAttribute("selectedSeasonId", seasonId);
            model.addAttribute("seasons", seasons.reversed());
            model.addAttribute("team", teamOptional.get());
            model.addAttribute("leagues", leagues);

            optional.ifPresent(statistics -> {
                model.addAttribute("statistics", statistics);
            });

        }
        return "team-details";
    }

    private long getSeasonId(Long seasonId, List<Season> seasons) {
        if (seasonId == null) {
            seasonId = seasons.getLast().getId();
        }

        return seasonId;
    }
}