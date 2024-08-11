package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.LeagueTeamSeasonService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.utils.SeasonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
public class StandingsController {

    private final SeasonService seasonService;
    private final LeagueService leagueService;
    private final LeagueTeamSeasonService leagueTeamSeasonService;

    @Value("${forex.api.key}")
    private String apiKey;

    public StandingsController(SeasonService seasonService,
                               LeagueService leagueService,
                               LeagueTeamSeasonService leagueTeamSeasonService) {
        this.seasonService = seasonService;
        this.leagueService = leagueService;
        this.leagueTeamSeasonService = leagueTeamSeasonService;
    }

    @GetMapping("/widgets/standings")
    public String getStandings(@RequestParam Long leagueId,
                               @RequestParam(required = false) Long seasonId,
                               Model model) {

        LeaguePageDto leagueById = this.leagueService.getLeagueById(leagueId);

        List<SeasonPageDto> seasons = this.seasonService.getAllSeasons();
        Set<SeasonPageDto> currentSeasons = SeasonUtils.getCurrentSeasonsForLeague(leagueId, leagueTeamSeasonService, seasons);

        seasonId = getId(seasonId, currentSeasons.stream().toList().getLast().getId());

        SeasonPageDto seasonOptional = this.seasonService.getSeasonById(seasonId);
        if (seasonOptional != null) {
            model.addAttribute("selectedSeasonId", seasonOptional.getId());
            model.addAttribute("selectedSeason", seasonOptional);
            model.addAttribute("seasonYear", seasonOptional.getYear());
            model.addAttribute("apiKey", apiKey);
            model.addAttribute("seasons", currentSeasons.stream().toList().reversed());
            model.addAttribute("leagueName", leagueById.getName());
            model.addAttribute("leagueApiId", leagueById.getApiId());
            model.addAttribute("league", leagueById);
        }

        return "standings";
    }

    private static Long getId(Long seasonId, long currentSeasonId) {
        return seasonId != null ? seasonId : currentSeasonId;
    }
}
