package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.teamDto.DreamTeamPageDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.service.DreamTeamService;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.TeamStatisticsService;
import bg.softuni.footscore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class DreamTeamController {
    private final TeamStatisticsService teamStatisticsService;
    private final UserService userService;
    private final PlayerService playerService;
    private final DreamTeamService dreamTeamService;
    private static final String[] POSITIONS = {"Goalkeeper", "Defender", "Midfielder", "Attacker"};

    public DreamTeamController(TeamStatisticsService teamStatisticsService,
                               UserService userService,
                               PlayerService playerService,
                               DreamTeamService dreamTeamService) {
        this.teamStatisticsService = teamStatisticsService;
        this.userService = userService;
        this.playerService = playerService;
        this.dreamTeamService = dreamTeamService;
    }

    @GetMapping("/create/dream-team")
    public String createDreamTeams(@RequestParam(required = false) String position,
                                 @RequestParam(required = false) String formation,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) String teamName,
                                 Model model) {

        List<PlayerPageDto> sortedPlayers = this.playerService.getAllSortedPlayers(position, search);


        model.addAttribute("goalkeepers", this.playerService.getAllPlayersByPosition("Goalkeeper").size());
        model.addAttribute("defenders", this.playerService.getAllPlayersByPosition("Defender").size());
        model.addAttribute("midfielders", this.playerService.getAllPlayersByPosition("Midfielder").size());
        model.addAttribute("attackers", this.playerService.getAllPlayersByPosition("Attacker").size());
        model.addAttribute("selectedPosition", position);
        model.addAttribute("positions", POSITIONS);
        model.addAttribute("teamName", teamName);
        model.addAttribute("selectedFormation", formation);
        model.addAttribute("searchTerm", search);
        model.addAttribute("players", sortedPlayers);

        return "dream-team-creation";
    }

    @PostMapping("/create/dream-team")
    public String addDreamTeam(@RequestParam(required = false) Long playerId,
                               @RequestParam(required = false) String position,
                               Model model) {

        if (this.playerService.getAllSelectedPlayers(true).size() >= 11) {
            return "redirect:/create/dream-team/preview";
        }

        this.playerService.setSelected(playerId, true);
        model.addAttribute("selectedPosition", position);

        return "redirect:/create/dream-team?position=" + position;
    }


    @GetMapping("/create/dream-team/preview")
    public String reviewDreamTeam(Model model) {

        List<PlayerPageDto> players = playerService.getAllSelectedPlayers(true);

        model.addAttribute("positions", Arrays.asList(POSITIONS).reversed());
        model.addAttribute("players", players);
        model.addAttribute("formations", this.teamStatisticsService.getAllFormations());

        return "dream-team-creation-preview";
    }

    @GetMapping("/profile/dream-teams")
    public String showUserDreamTeams(Model model) {
        UserEntityPageDto user = this.userService.getUser();

        List<DreamTeamPageDto> dreamTeams = this.dreamTeamService.getAllDreamTeamsByUserId(user.getId());

        model.addAttribute("dreamTeams", dreamTeams);
        return "user-dream-teams";
    }

    @GetMapping("/all/dream-teams")
    public String showAllDreamTeams(Model model) {
        model.addAttribute("dreamTeams", this.dreamTeamService.getAll());
        return "view-all-dream-teams";
    }

    @PostMapping("/create/dream-team/save")
    public String saveDreamTeam(@RequestParam String teamName) {

        this.dreamTeamService.createDreamTeam(teamName);

        return "redirect:/profile/dream-teams";
    }

    @PostMapping("/cancel")
    public String cancelDreamTeams(Model model) {
        this.playerService.setAllSelected(false);
        return "redirect:/profile";
    }

    @GetMapping("/dream-team/{dreamTeamId}/details")
    public String showDreamTeamDetails(@PathVariable Long dreamTeamId, Model model) {
        DreamTeamPageDto byId = this.dreamTeamService.getById(dreamTeamId);
        UserEntityPageDto user = this.userService.getUser();

        if (byId == null) {
            return "redirect:/dream-team-detail-error";
        }

        model.addAttribute("user", user);
        model.addAttribute("team", byId);
        model.addAttribute("players", byId.getPlayers());
        model.addAttribute("positions", Arrays.asList(POSITIONS).reversed());
        return "dream-team-details";
    }


    @DeleteMapping("/dream-team/{teamId}/delete")
    public String deleteDreamTeam(@PathVariable Long teamId) {
        this.dreamTeamService.deleteTeam(teamId);
        return "redirect:/profile/dream-teams";
    }
}
