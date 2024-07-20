package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class TeamController {
    private final TeamService teamService;
    private final LeagueService leagueService;

    public TeamController(TeamService teamService, LeagueService leagueService) {
        this.teamService = teamService;
        this.leagueService = leagueService;
    }

    @GetMapping("/league/{id}/teams")
    public String teams(@PathVariable long id, Model model) {
        Optional<League> leagueById = this.leagueService.getLeagueById(id);

        leagueById.ifPresent(league ->  {
            this.teamService.saveApiTeamsForLeagueAndSeason(id);
            List<Team> teams = leagueById.get().getTeams();
            model.addAttribute("teams", teams);
            model.addAttribute("league", league);
        });

        return "teams";
    }
}
