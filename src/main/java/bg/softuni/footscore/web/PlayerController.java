package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.LeagueTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.service.*;
import bg.softuni.footscore.utils.SeasonUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final ModelMapper modelMapper;
    private final UserService userService;
    private static final String[] POSITIONS = {"Attacker", "Midfielder", "Defender", "Goalkeeper"};

    public PlayerController(PlayerService playerService,
                            TeamService teamService,
                            SeasonService seasonService,
                            PlayerTeamSeasonService seasonTeamPlayerService,
                            LeagueTeamSeasonService seasonLeagueTeamService,
                            LeagueService leagueService,
                            ModelMapper modelMapper,
                            UserService userService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.playerTeamSeasonService = seasonTeamPlayerService;
        this.leagueTeamSeasonService = seasonLeagueTeamService;
        this.leagueService = leagueService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/team/{teamId}/players")
    public String players(@PathVariable long teamId,
                          @RequestParam(required = false) Long seasonId,
                          @RequestParam(required = false) String position,
                          Model model) {

        TeamPageDto teamOptional = this.teamService.getTeamById(teamId);
        if (teamOptional == null) {
            throw new EntityNotFoundException("Not found team");
        }

        List<SeasonPageDto> seasons = this.seasonService.getAllSeasons();
        Set<SeasonPageDto> currentSeasons = SeasonUtils.getCurrentSeasonsForTeam(teamId, leagueTeamSeasonService, seasons);
        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());


        List<LeagueTeamSeasonPageDto> byTeamIdAndSeasonId = this.leagueTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);
        if (byTeamIdAndSeasonId.isEmpty()) {
            throw new EntityNotFoundException("Not found team or season");
        }
        List<LeaguePageDto> leagues = this.leagueService.getSelectedLeagueByTeamAndSeason(byTeamIdAndSeasonId);


        List<PlayerTeamSeasonPageDto> allPlayers = this.playerTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);
        allPlayers = this.playerService.getAllPlayers(teamId, seasonId, allPlayers, teamOptional);
        if (allPlayers.isEmpty()) {
            return "redirect:/players/error";
        }
        List<PlayerPageDto> validPlayers = new ArrayList<>(allPlayers.stream().map(PlayerTeamSeasonPageDto::getPlayer).toList());
        validPlayers = this.playerService.getPlayersByPosition(position, validPlayers, allPlayers);

        model.addAttribute("selectedPosition", position);
        model.addAttribute("team", teamOptional);
        model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
        model.addAttribute("selectedSeasonId", seasonId);
        model.addAttribute("positions", POSITIONS);
        model.addAttribute("user", this.userService.getUser());
        model.addAttribute("leagues", leagues);
        model.addAttribute("players", validPlayers);

        return "players";
    }



    @GetMapping("/player/{playerId}/details")
    public String details(@PathVariable long playerId,
                          @RequestParam(required = false) String position,
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

    @Transactional
    @PostMapping("/team/{teamId}/players")
    public String addPlayers(@PathVariable Long teamId,
                             @RequestParam(required = false) Long seasonId,
                             @RequestParam(required = false) List<Long> playerIds,
                             Model model) {

        if (teamId == null) {
            return "redirect:/team-error";
        }

        if (seasonId == null) {
            seasonId = this.seasonService.getAllSeasons().getLast().getId();
        }

        model.addAttribute("seasonId", this.seasonService.getSeasonById(seasonId));

        UserEntityPageDto user = this.userService.getUser();

        this.userService.addPlayersToFavorites(playerIds, user);

        return "redirect:/team/" + teamId + "/players" + "?seasonId=" + seasonId;
    }




    @Transactional
    @GetMapping("/team/{teamId}/players/add-favorites")
    public String playersAddFavorites(@PathVariable long teamId,
                                      @RequestParam(required = false) String position,
                                      @RequestParam Long seasonId,
                                      Model model) {

        if (seasonId == null) {
            seasonId = this.seasonService.getAllSeasons().getLast().getId();
        }
        List<PlayerTeamSeasonPageDto> byTeamIdAndSeasonId = this.playerTeamSeasonService.getByTeamIdAndSeasonId(teamId, seasonId);
        List<PlayerPageDto> players = byTeamIdAndSeasonId.stream().map(s -> this.modelMapper.map(s.getPlayer(), PlayerPageDto.class)).toList();

        UserEntityPageDto user = this.userService.getUser();

        players = this.playerService.getPlayersByPosition(position, players, byTeamIdAndSeasonId);

        if (user != null) {
            List<PlayerPageDto> list = this.userService.getFavoritePlayers(players, user);
            model.addAttribute("favoritePlayers", list);
        }

        model.addAttribute("selectedPosition", position);
        model.addAttribute("positions", POSITIONS);
        model.addAttribute("players", players);
        model.addAttribute("selectedSeasonId", seasonId);
        model.addAttribute("team", byTeamIdAndSeasonId.getFirst().getTeam());
        model.addAttribute("season", byTeamIdAndSeasonId.getFirst().getSeason());

        return "add-favorites-players";
    }

    private static Long getId(Long seasonId, long currentSeasonId) {
        return seasonId != null ? seasonId : currentSeasonId;
    }
}
