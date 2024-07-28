package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.service.*;
import bg.softuni.footscore.utils.SeasonUtils;
import jakarta.persistence.EntityNotFoundException;
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

        TeamPageDto teamOptional = this.teamService.getTeamById(teamId);
        if (teamOptional == null) {
            return "redirect:/team-error";
        }

        List<SeasonPageDto> seasons = this.seasonService.getAllSeasons();
        Set<SeasonPageDto> currentSeasons = SeasonUtils.getCurrentSeasonsForTeam(teamId, leagueTeamSeasonService, seasons);

        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());

        List<LeagueTeamSeasonPageDto> byTeamIdAndSeasonId = this.leagueTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);

        if (byTeamIdAndSeasonId.isEmpty()) {
            throw new EntityNotFoundException("Not found team or season");
        }

        String[] positions = {"Attacker", "Midfielder", "Defender", "Goalkeeper"};

        List<LeaguePageDto> leagues = new ArrayList<>();
        byTeamIdAndSeasonId.forEach(s -> {
            LeaguePageDto leagueById = this.leagueService.getLeagueById(s.getLeague().getId());
            if (leagueById.isSelected()) {
                leagues.add(leagueById);
            }
        });

        model.addAttribute("team", teamOptional);
        model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
        model.addAttribute("selectedSeasonId", seasonId);
        model.addAttribute("positions", positions);
        model.addAttribute("leagues", leagues);

        List<PlayerTeamSeasonPageDto> allPlayers = this.playerTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);

        if (allPlayers.isEmpty()) {
            SeasonPageDto seasonOptional = this.seasonService.getSeasonById(seasonId);
            if (seasonOptional != null) {
                this.playerService.saveApiPlayersForTeamAndSeason(teamOptional, seasonOptional);
            }
            allPlayers  = this.playerTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);
        }

        if (allPlayers.isEmpty()) {
            return "redirect:/players/error";
        }

        if (position != null && !position.equals("All positions")) {
            allPlayers.removeIf(s -> !s.getPlayer().getPosition().equals(position));
            model.addAttribute("selectedPosition", position);
        }

        model.addAttribute("players", allPlayers);

        return "players";
    }

    @GetMapping("/player/{playerId}/details")
    public String details(@PathVariable long playerId,
                          Model model) {

        PlayerPageDto dto = this.playerService.getPlayerById(playerId);

        if (dto == null) {
            return "redirect:/player-details-error";
        }

        if (dto.getRetired() == null) {
            this.playerService.fillMissingPlayerDetails(playerId);
        }

        PlayerPageDto player = this.playerService.getPlayerById(playerId);

        model.addAttribute("player", player);
        model.addAttribute("birthdate", player);

        return "player-details";
    }

    private static Long getId(Long seasonId, long currentSeasonId) {
        return seasonId != null ? seasonId : currentSeasonId;
    }
}
