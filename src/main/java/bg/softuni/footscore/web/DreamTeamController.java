package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.FormationDto;
import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.service.DreamTeamService;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.TeamStatisticsService;
import bg.softuni.footscore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

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
    public String showDreamTeams(@RequestParam(required = false) String position,
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
                               Model model) {

        this.playerService.setSelected(playerId, true);
        return "redirect:/create/dream-team";
    }


    @GetMapping("/create/dream-team/preview")
    public String reviewDreamTeam(Model model) {

        List<PlayerPageDto> players = playerService.getAllSelectedPlayers(true);
        List<PlayerPageDto> defenders = new ArrayList<>();
        List<PlayerPageDto> midfielders = new ArrayList<>();
        List<PlayerPageDto> attackers = new ArrayList<>();

        for (PlayerPageDto player : players) {
            switch (player.getPosition()) {
                case "Goalkeeper":
                    model.addAttribute("goalkeeper", player);
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

        model.addAttribute("defenders", defenders);
        model.addAttribute("midfielders", midfielders);
        model.addAttribute("attackers", attackers);
        model.addAttribute("positions", POSITIONS);
        model.addAttribute("formations", this.teamStatisticsService.getAllFormations());

        return "dream-team-creation-preview";
    }


    @PostMapping("/create/dream-team/save")
    public String saveDreamTeam(@RequestParam String teamName) {
        List<PlayerPageDto> allSelectedPlayers = this.playerService.getAllSelectedPlayers(true);
        long defenderCount = allSelectedPlayers.stream().filter(p -> p.getPosition().equals("Defender")).count();
        long midfielderCount = allSelectedPlayers.stream().filter(p -> p.getPosition().equals("Midfielder")).count();
        long attackerCount = allSelectedPlayers.stream().filter(p -> p.getPosition().equals("Attacker")).count();

        String formation = defenderCount + "-" + midfielderCount + "-" + attackerCount;

        UserEntityPageDto user = this.userService.getUser();

        this.dreamTeamService.create(teamName, formation, allSelectedPlayers, user);
        return "redirect:/";
    }

    @PostMapping("/cancel")
    public String showDreamTeams(Model model) {
        List<PlayerPageDto> allSelectedPlayers = this.playerService.getAllSelectedPlayers(true);
        allSelectedPlayers.forEach(p -> this.playerService.setSelected(p.getId(), false));
        return "redirect:/profile";
    }


    private static List<PlayerPageDto> getPlayersByPosition(String position, List<PlayerPageDto> validPlayers) {
        if (position != null && !position.equals("All positions")) {
            validPlayers.removeIf(p -> !p.getPosition().equals(position));
        }
        return validPlayers;
    }
}
