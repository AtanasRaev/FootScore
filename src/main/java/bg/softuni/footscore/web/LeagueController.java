package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.dto.leagueDto.LeagueAddDto;
import bg.softuni.footscore.model.dto.leagueDto.LeaguePageDto;
import bg.softuni.footscore.model.dto.leagueDto.SelectedLeaguesDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.TeamService;
import bg.softuni.footscore.service.impl.LeagueTeamSeasonServiceImpl;
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
    private final LeagueTeamSeasonServiceImpl seasonLeagueTeamService;

    public LeagueController(LeagueService leagueService,
                            CountryService countryService,
                            TeamService teamService,
                            SeasonService seasonService,
                            LeagueTeamSeasonServiceImpl seasonLeagueTeamService) {
        this.leagueService = leagueService;
        this.countryService = countryService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.seasonLeagueTeamService = seasonLeagueTeamService;
    }

    @GetMapping
    public String league(@ModelAttribute("countryName") String countryName, Model model) {
        //todo: error handling
        SelectedLeaguesDto leagues = this.leagueService.getAllSelectedWithCountry(countryName);

        if (leagues == null) return "redirect:/leagues/error";

        model.addAttribute("countriesList", leagues.getCountries());
        model.addAttribute("leaguesList", leagues.getAllSelectedLeagues());

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
    public String previewSelectedLeagues(@RequestParam(required = false) List<Long> leagueIds, Model model) {
        //todo: error handling
        if (leagueIds == null || leagueIds.isEmpty()) {
            return "redirect:/leagues/add";
        }
        List<LeaguePageDto> selectedLeagues = this.leagueService.getLeagueByIds(leagueIds);

        model.addAttribute("selectedLeagues", selectedLeagues);
        return "preview-leagues";
    }

    @PostMapping("/saveSelected")
    public String saveSelectedLeagues(@RequestParam List<Long> leagueIds) {

        List<SeasonPageDto> seasons = this.seasonService.getAllSeasons();

        if (leagueIds == null || leagueIds.isEmpty() || seasons == null || seasons.isEmpty()) {
            return "redirect:/leagues/error";
        }

        List<LeaguePageDto> leagues = this.leagueService.getLeagueByIds(leagueIds);

        if (leagues == null || leagues.isEmpty()) {
            return "redirect:/leagues/error";
        }

        this.leagueService.updateSelectedLeagues(leagues);
        this.teamService.fetchTeams(leagues, seasons);
        return "redirect:/leagues";
    }

    @GetMapping("/remove")
    public String removeSelectedLeagues(@ModelAttribute("countryName") String countryName, Model model) {
        SelectedLeaguesDto leagues = this.leagueService.getAllSelectedWithCountry(countryName);

        if (leagues == null) {
            return "redirect:/leagues/error";
        }

        model.addAttribute("countriesRemove", leagues.getCountries());
        model.addAttribute("leaguesRemove", leagues.getAllSelectedLeagues());

        return "remove-leagues";
    }

    @PostMapping("/remove")
    public String doRemoveSelectedLeagues(@RequestParam Long leagueId) {
        if (leagueId == null) {
            return "redirect:/leagues/error";
        }

        this.leagueService.removeLeague(leagueId);
        return "redirect:/leagues/remove";
    }
}
