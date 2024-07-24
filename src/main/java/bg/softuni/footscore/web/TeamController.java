package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class TeamController {
    private final LeagueService leagueService;
    private final SeasonService seasonService;
    private final SeasonLeagueTeamService seasonLeagueTeamService;

    public TeamController(LeagueService leagueService,
                          SeasonService seasonService,
                          SeasonLeagueTeamService seasonLeagueTeamService) {
        this.leagueService = leagueService;
        this.seasonService = seasonService;
        this.seasonLeagueTeamService = seasonLeagueTeamService;
    }

    @GetMapping("/league/{leagueId}/teams")
    public String teams(@PathVariable long leagueId, @RequestParam(required = false) Long seasonId, Model model) {
        Optional<League> leagueById = this.leagueService.getLeagueById(leagueId);
        List<Season> seasons = this.seasonService.getAllSeasons();

        if (leagueById.isPresent()) {
            model.addAttribute("league", leagueById.get());
            model.addAttribute("seasons", seasons.reversed());
            model.addAttribute("selectedSeasonId", seasonId);

            if (seasonId == null) {
                seasonId = seasons.getLast().getId();
            }

            List<Team> allTeams = this.seasonLeagueTeamService.getAllTeamsBySeasonIdAndLeagueId(leagueId, seasonId);

            model.addAttribute("teams", allTeams);
        }
        return "teams";
    }
}