package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.FormationDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.service.DreamTeamService;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.TeamStatisticsService;
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
    private final PlayerService playerService;
    private final DreamTeamService dreamTeamService;
    private static final String[] POSITIONS = {"Goalkeeper", "Defender", "Midfielder", "Attacker"};

    public DreamTeamController(TeamStatisticsService teamStatisticsService,
                               PlayerService playerService,
                               DreamTeamService dreamTeamService) {
        this.teamStatisticsService = teamStatisticsService;
        this.playerService = playerService;
        this.dreamTeamService = dreamTeamService;
    }

    @GetMapping("/create/dream-team")
    public String showDreamTeams(@RequestParam(required = false) String position,
                                 @RequestParam(required = false) String formation,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) String teamName,
                                 @RequestParam(required = false) List<Long> playerIds,
                                 Model model) {

        List<PlayerPageDto> allPlayers = this.playerService.getAllPlayers();
        List<PlayerPageDto> sortedPlayers = new ArrayList<>(allPlayers);

        if (search != null && !search.trim().isEmpty()) {
            sortedPlayers.removeIf(player -> !player.getFullName().toLowerCase().contains(search.toLowerCase()));
        }

        sortedPlayers = getPlayersByPosition(position, sortedPlayers);
        sortedPlayers.sort(Comparator.comparing(PlayerPageDto::getShortName));

        List<FormationDto> allFormations = this.teamStatisticsService.getAllFormations();
        Set<String> formations = new TreeSet<>();
        for (FormationDto allFormation : allFormations) {
            formations.add(allFormation.getFormation());
        }

        model.addAttribute("selectedPosition", position);
        model.addAttribute("positions", POSITIONS);
        model.addAttribute("teamName", teamName);
        model.addAttribute("formations", formations);
        model.addAttribute("selectedFormation", formation);
        model.addAttribute("searchTerm", search);
        model.addAttribute("players", sortedPlayers);
        model.addAttribute("selectedPlayerIds", (playerIds != null) ? playerIds : new ArrayList<>());

        return "dream-team-creation";
    }


    @GetMapping("/create/dream-team/preview")
    public String reviewDreamTeam(@RequestParam String teamName,
                                  @RequestParam String formation,
                                  @RequestParam List<Long> playerIds,
                                  Model model) {

        List<PlayerPageDto> players = playerService.getAllByIds(playerIds);
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

        model.addAttribute("teamName", teamName);
        model.addAttribute("defenders", defenders);
        model.addAttribute("midfielders", midfielders);
        model.addAttribute("attackers", attackers);
        model.addAttribute("formation", formation);
        model.addAttribute("positions", POSITIONS);
        model.addAttribute("playerIds", playerIds);

        model.addAttribute("selectedPlayerIds", playerIds);

        return "dream-team-creation-preview";
    }


    @PostMapping("/create/dream-team/save")
    public String saveDreamTeam(@RequestParam String teamName,
                                @RequestParam String formation,
                                @RequestParam List<Long> playerIds) {

        return "redirect:/success";
    }


    private static List<PlayerPageDto> getPlayersByPosition(String position, List<PlayerPageDto> validPlayers) {
        if (position != null && !position.equals("All positions")) {
            validPlayers.removeIf(p -> !p.getPosition().equals(position));
        }
        return validPlayers;
    }
}
