package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.*;
import bg.softuni.footscore.service.*;
import bg.softuni.footscore.utils.SeasonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class PlayerController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final PlayerTeamSeasonService playerTeamSeasonService;
    private final LeagueTeamSeasonService leagueTeamSeasonService;
    private final LeagueService leagueService;

    public PlayerController(PlayerService playerService,
                            TeamService teamService,
                            SeasonService seasonService,
                            PlayerTeamSeasonService seasonTeamPlayerService,
                            LeagueTeamSeasonService seasonLeagueTeamService,
                            LeagueService leagueService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.playerTeamSeasonService = seasonTeamPlayerService;
        this.leagueTeamSeasonService = seasonLeagueTeamService;
        this.leagueService = leagueService;
    }

    @GetMapping("/team/{teamId}/players")
    public String players(@PathVariable long teamId,
                          @RequestParam(required = false) Long seasonId,
                          @RequestParam(required = false) String position,
                          Model model) {

        Optional<Team> teamOptional = this.teamService.getTeamById(teamId);
        if (teamOptional.isEmpty()) {
            return "redirect:/team-error";
        }

        List<Season> seasons = this.seasonService.getAllSeasons();
        Set<Season> currentSeasons = SeasonUtils.getCurrentSeasonsForTeam(teamId, leagueTeamSeasonService, seasons);

        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());

        List<LeagueTeamSeason> byTeamIdAndSeasonId = this.leagueTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);

        String[] positions = {"Attacker", "Midfielder", "Defender", "Goalkeeper"};

        List<League> leagues = new ArrayList<>();
        byTeamIdAndSeasonId.forEach(s -> {
            Optional<League> leagueOptional = this.leagueService.getLeagueById(s.getLeague().getId());
            leagueOptional.ifPresent(league -> {
                if (league.isSelected()) {
                    leagues.add(league);
                }
            });
        });

        model.addAttribute("team", teamOptional.get());
        model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
        model.addAttribute("selectedSeasonId", seasonId);
        model.addAttribute("positions", positions);
        model.addAttribute("leagues", leagues);

        List<Player> allPlayers = this.playerTeamSeasonService.getAllPlayersBySeasonIdAndTeamId(teamId, seasonId);

        if (allPlayers.isEmpty()) {
            Optional<Season> seasonOptional = this.seasonService.getSeasonById(seasonId);
            seasonOptional.ifPresent(season -> this.playerService.saveApiPlayersForTeamAndSeason(teamOptional.get(), season));
            allPlayers = this.playerTeamSeasonService.getAllPlayersBySeasonIdAndTeamId(teamId, seasonId);
        }

        if (allPlayers.isEmpty()) {
            return "redirect:/players/error";
        }

        if (position != null && !position.equals("All positions")) {
            allPlayers.removeIf(player -> !player.getPosition().equals(position));
            model.addAttribute("selectedPosition", position);
        }

        model.addAttribute("players", allPlayers);

        return "players";
    }

    private static Long getId(Long seasonId, long currentSeasonId) {
        return seasonId != null ? seasonId : currentSeasonId;
    }
}
