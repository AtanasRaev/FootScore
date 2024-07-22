package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.SeasonTeamPlayerService;
import bg.softuni.footscore.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class PlayerController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final SeasonTeamPlayerService seasonTeamPlayerService;

    public PlayerController(PlayerService playerService, TeamService teamService,
                            SeasonService seasonService,
                            SeasonTeamPlayerService seasonTeamPlayerService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.seasonTeamPlayerService = seasonTeamPlayerService;
    }

    //TODO: FIX -> 1. NOT LOADING THE PLAYERS ON THE FIRST LOAD, 2. SLOW PAGE LOADING, 3. CHECK FOR ERRORS

    @GetMapping("/team/{teamId}/players")
    public String players(@PathVariable long teamId, @RequestParam(required = false) Long seasonId, Model model) {
        Optional<Team> teamById = this.teamService.getTeamById(teamId);

        List<Season> seasons = this.seasonService.getAllSeasons();

        if (teamById.isPresent()) {
            model.addAttribute("team", teamById.get());
            model.addAttribute("seasons", seasons.reversed());
            model.addAttribute("selectedSeasonId", seasonId);

            this.showPlayers(teamId, model, Objects.requireNonNullElseGet(seasonId, () -> seasons.getLast().getId()));
        }
        return "players";
    }

    private void showPlayers(long teamId, Model model, long seasonId) {
        List<Player> allPlayers = this.seasonTeamPlayerService.getAllPlayersBySeasonIdAndTeamId(teamId, seasonId);
        if (allPlayers.isEmpty()) {
            this.playerService.saveApiPlayersForTeamAndSeason(teamId, seasonId);
        }
        model.addAttribute("players", allPlayers);
    }
}
