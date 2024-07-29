package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.FormationDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.TeamStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class DreamTeamController {
    private final TeamStatisticsService teamStatisticsService;
    private final PlayerService playerService;

    public DreamTeamController(TeamStatisticsService teamStatisticsService,
                               PlayerService playerService) {
        this.teamStatisticsService = teamStatisticsService;
        this.playerService = playerService;
    }

    @GetMapping("/create/dream-team")
    public String showDreamTeams(Model model) {
        List<PlayerPageDto> allPLayers = this.playerService.getAllPLayers();
        List<PlayerPageDto> sortedPlayers = new ArrayList<>(allPLayers);

        sortedPlayers.sort(Comparator.comparing(PlayerPageDto::getShortName));

        List<FormationDto> allFormations = this.teamStatisticsService.getAllFormations();
        Set<String> formations = new TreeSet<>();

        for (FormationDto allFormation : allFormations) {
            formations.add(allFormation.getFormation());
        }

        model.addAttribute("formations", formations);
        model.addAttribute("players", sortedPlayers);
        return "dream-team-creation";
    }
}
