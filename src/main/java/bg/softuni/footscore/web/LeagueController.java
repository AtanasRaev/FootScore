package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.LeagueAddDto;
import bg.softuni.footscore.model.dto.LeaguesPageDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.model.entity.SeasonLeagueTeam;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.TeamService;
import bg.softuni.footscore.service.impl.SeasonLeagueTeamServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/leagues")
public class LeagueController {
    private final LeagueService leagueService;
    private final CountryService countryService;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final SeasonLeagueTeamServiceImpl seasonLeagueTeamService;

    public LeagueController(LeagueService leagueService,
                            CountryService countryService,
                            TeamService teamService,
                            SeasonService seasonService,
                            SeasonLeagueTeamServiceImpl seasonLeagueTeamService) {
        this.leagueService = leagueService;
        this.countryService = countryService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.seasonLeagueTeamService = seasonLeagueTeamService;
    }

    @GetMapping
    public String league(@ModelAttribute("countryName") String countryName, Model model) {
        //todo: error handling
        Result result = getResult(countryName);
        if (result == null) {
            return "redirect:/leagues/error";
        }

        model.addAttribute("countriesList", result.countries());
        model.addAttribute("leaguesList", result.allSelectedLeagues());

        return "leagues";
    }


    //TODO:/leagues/add authority
    @GetMapping("/add")
    public String getLeaguesByCountry(@ModelAttribute("countryName") String countryName, Model model) {
        //todo: error handling
        List<String> countries = this.countryService.getAllCountriesNames();
        List<LeagueAddDto> leagues = new ArrayList<>();

        if (countryName != null && !countryName.isEmpty()) {
            leagues = this.leagueService.getAllNotSelectedLeaguesByCountry(countryName, true);
        }

        model.addAttribute("countries", countries);
        model.addAttribute("leagues", leagues);
        return "add-leagues";
    }

    @PostMapping("/preview")
    public String previewSelectedLeagues(@RequestParam List<Long> leagueIds, Model model) {
        //todo: error handling
        List<LeagueAddDto> selectedLeagues = this.leagueService.getLeaguesByIds(leagueIds);
        model.addAttribute("selectedLeagues", selectedLeagues);
        return "preview-leagues";
    }

    @PostMapping("/saveSelected")
    public String saveSelectedLeagues(@RequestParam List<Long> leagueIds) {

        //todo: error handling
        leagueIds.forEach(id -> {
            Optional<League> leagueOptional = this.leagueService.getLeagueById(id);
            for (Season season : this.seasonService.getAllSeasons()) {
                leagueOptional.ifPresent(league -> {
                    List<Optional<SeasonLeagueTeam>> optional = this.seasonLeagueTeamService.getTeamsByLeagueIdAndSeasonId(league.getId(), season.getId());

                    if (optional.isEmpty()) {
                        this.teamService.saveApiTeamsForLeagueAndSeason(league, season);
                    }
                });
            }
        });
        this.leagueService.saveSelectedLeagues(leagueIds);
        return "redirect:/leagues";
    }

    @GetMapping("/remove")
    public String removeSelectedLeagues(@ModelAttribute("countryName") String countryName, Model model) {
        Result result = getResult(countryName);
        if (result == null) return "redirect:/leagues/error";

        model.addAttribute("countriesRemove", result.countries());
        model.addAttribute("leaguesRemove", result.allSelectedLeagues());

        return "remove-leagues";
    }

    @PostMapping("/remove")
    public String doRemoveSelectedLeagues(@RequestParam long leagueId) {
        Optional<League> leagueById = this.leagueService.getLeagueById(leagueId);
        if (leagueById.isPresent()) {
            leagueById.get().setSelected(false);
            this.leagueService.saveLeague(leagueById.get());
        }
        return "redirect:/leagues/remove";
    }

    private Result getResult(String countryName) {
        List<String> countries = this.countryService.getAllCountriesNames();

        if (this.leagueService.isEmpty()) {
            return null;
        }

        List<LeaguesPageDto> allSelectedLeagues = countryName.equals("All countries") || countryName.isEmpty() ?
                this.leagueService.getAllSelectedLeaguesDto() :
                this.leagueService.getAllSelectedLeaguesByCountry(countryName, false);
        return new Result(countries, allSelectedLeagues);
    }

    private record Result(List<String> countries, List<LeaguesPageDto> allSelectedLeagues) {
    }
}
