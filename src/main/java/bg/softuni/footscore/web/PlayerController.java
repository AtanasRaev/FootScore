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
public class PlayerController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final SeasonTeamPlayerService seasonTeamPlayerService;
    private final SeasonLeagueTeamService seasonLeagueTeamService;
    private final LeagueService leagueService;

    public PlayerController(PlayerService playerService,
                            TeamService teamService,
                            SeasonService seasonService,
                            SeasonTeamPlayerService seasonTeamPlayerService,
                            SeasonLeagueTeamService seasonLeagueTeamService,
                            LeagueService leagueService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.seasonTeamPlayerService = seasonTeamPlayerService;
        this.seasonLeagueTeamService = seasonLeagueTeamService;
        this.leagueService = leagueService;
    }

    @GetMapping("/team/{teamId}/players")
    public String players(@PathVariable long teamId,
                          @RequestParam(required = false) Long seasonId,
                          @RequestParam(required = false) String position,
                          Model model) {

        Optional<Team> teamOptional = this.teamService.getTeamById(teamId);

        List<Season> seasons = this.seasonService.getAllSeasons();

        if (seasonId == null) {
            seasonId = seasons.getLast().getId();
        }

        List<SeasonLeagueTeam> byTeamIdAndSeasonId = this.seasonLeagueTeamService.getByTeamIdAndSeasonId(teamId, seasonId);

        String[] positions = {"Attacker", "Midfielder", "Defender", "Goalkeeper"};

        if (teamOptional.isPresent() && !byTeamIdAndSeasonId.isEmpty()) {
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
            model.addAttribute("seasons", seasons.reversed());
            model.addAttribute("selectedSeasonId", seasonId);
            model.addAttribute("positions", positions);
            model.addAttribute("leagues", leagues);


            List<Player> allPlayers = this.seasonTeamPlayerService.getAllPlayersBySeasonIdAndTeamId(teamId, seasonId);

            if (allPlayers.isEmpty()) {
                Optional<Season> seasonOptional = this.seasonService.getSeasonById(seasonId);
                seasonOptional.ifPresent(season -> this.playerService.saveApiPlayersForTeamAndSeason(teamOptional.get(), season));
                allPlayers = this.seasonTeamPlayerService.getAllPlayersBySeasonIdAndTeamId(teamId, seasonId);
            }

            if (allPlayers.isEmpty()) {
                //no data for players
                return "redirect:players/error";
            }

            if (position != null && !position.equals("All positions")) {
                allPlayers.removeIf(player -> !player.getPosition().equals(position));

                model.addAttribute("selectedPosition", position);
            }

            model.addAttribute("players", allPlayers);
        }
        return "players";
    }
}
