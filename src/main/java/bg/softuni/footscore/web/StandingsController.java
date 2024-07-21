package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class StandingsController {

    private final SeasonService seasonService;
    private final LeagueService leagueService;

    @Value("${forex.api.key}")
    private String apiKey;

    public StandingsController(SeasonService seasonService, LeagueService leagueService) {
        this.seasonService = seasonService;
        this.leagueService = leagueService;
    }

    @GetMapping("/widgets/standings")
    public String getStandings(@RequestParam String leagueId,
                               @RequestParam(required = false) String seasonId,
                               Model model) {
        Optional<League> leagueOpt = leagueService.getLeagueById(Long.parseLong(leagueId));
        if (leagueOpt.isEmpty()) {
            return "error/leagueNotFound";
        }

        League league = leagueOpt.get();
        model.addAttribute("apiKey", apiKey);
        model.addAttribute("seasons", this.seasonService.getAllSeasons().reversed());
        model.addAttribute("leagueName", league.getName());
        model.addAttribute("leagueId", league.getApiId());
        model.addAttribute("league", leagueOpt.get());

        Season season = getSeasonYear(seasonId);
        if (season != null) {
            int year = season.getYear();
            model.addAttribute("selectedSeasonId", season.getId());
            model.addAttribute("selectedSeason", season);
            model.addAttribute("seasonYear", year);

        } else {
            return "error/seasonNotFound";
        }

        return "standings";
    }

    private Season getSeasonYear(String seasonId) {
        if (seasonId != null && !seasonId.isEmpty()) {
            Optional<Season> seasonOpt = seasonService.getSeasonById(Long.parseLong(seasonId));
            return seasonOpt.orElse(null);
        } else {
            return this.seasonService.getAllSeasons().getLast();
        }
    }
}
