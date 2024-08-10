package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.teamDto.DreamTeamPageDto;
import bg.softuni.footscore.model.dto.teamDto.FormationDto;
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

        List<PlayerPageDto> allNotSelected = this.playerService.getAllSelectedPlayers(false);
        List<PlayerPageDto> allSelected = this.playerService.getAllSelectedPlayers(true);
        List<PlayerPageDto> sortedPlayers = new ArrayList<>(allNotSelected);

        if (search != null && !search.trim().isEmpty()) {
            sortedPlayers.removeIf(player -> !player.getFullName().toLowerCase().contains(search.toLowerCase()));
        }

        sortedPlayers = getPlayersByPosition(position, sortedPlayers);
        sortedPlayers.sort(Comparator.comparing(PlayerPageDto::getShortName));

        List<PlayerPageDto> goalkeepers = new ArrayList<>();
        List<PlayerPageDto> defenders = new ArrayList<>();
        List<PlayerPageDto> midfielders = new ArrayList<>();
        List<PlayerPageDto> attackers = new ArrayList<>();

        List<FormationDto> allFormations = this.teamStatisticsService.getAllFormations();
        Set<String> formations = new TreeSet<>();
        for (FormationDto allFormation : allFormations) {
            formations.add(allFormation.getFormation());
        }


        for (PlayerPageDto player : allSelected) {
            switch (player.getPosition()) {
                case "Goalkeeper":
                    goalkeepers.add(player);
                    break;
                case "Defender":
                    defenders.add(player);
                    break;
                case "Midfielder":
                    midfielders.add(player);
                    break;
                case "Attacker":
                    attackers.add(player);
                    break;
            }
        }

        model.addAttribute("goalkeepers", goalkeepers.size());
        model.addAttribute("defenders", defenders.size());
        model.addAttribute("midfielders", midfielders.size());
        model.addAttribute("attackers", attackers.size());
        model.addAttribute("selectedPosition", position);
        model.addAttribute("positions", POSITIONS);
        model.addAttribute("teamName", teamName);
        model.addAttribute("formations", formations);
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

        //TODO: unique names, errorHandling
        if (this.dreamTeamService.checkTeamName(teamName)) {
            return "redirect:/profile/dream-teams";
        }

        List<PlayerPageDto> allSelectedPlayers = this.playerService.getAllSelectedPlayers(true);
        long defenderCount = allSelectedPlayers.stream().filter(p -> p.getPosition().equals("Defender")).count();
        long midfielderCount = allSelectedPlayers.stream().filter(p -> p.getPosition().equals("Midfielder")).count();
        long attackerCount = allSelectedPlayers.stream().filter(p -> p.getPosition().equals("Attacker")).count();

        String formation = defenderCount + "-" + midfielderCount + "-" + attackerCount;

        UserEntityPageDto user = this.userService.getUser();

        this.dreamTeamService.create(teamName, formation, allSelectedPlayers, user);
        return "redirect:/profile/dream-teams";
    }

    @PostMapping("/cancel")
    public String cancelDreamTeams(Model model) {
        List<PlayerPageDto> allSelectedPlayers = this.playerService.getAllSelectedPlayers(true);
        allSelectedPlayers.forEach(p -> this.playerService.setSelected(p.getId(), false));
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

    private static List<PlayerPageDto> getPlayersByPosition(String position, List<PlayerPageDto> validPlayers) {
        if (position != null && !position.equals("All positions") && !position.isEmpty()) {
            validPlayers.removeIf(p -> !p.getPosition().equals(position));
        }
        return validPlayers;
    }
}
