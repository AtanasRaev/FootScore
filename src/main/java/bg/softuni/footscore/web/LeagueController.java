package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.dto.LeaguesPageDto;
import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/leagues")
public class LeagueController {
    private final LeagueService leagueService;
    private final CountryService countryService;

    public LeagueController(LeagueService leagueService, CountryService countryService) {
        this.leagueService = leagueService;
        this.countryService = countryService;
    }
    @GetMapping
    public String league(@ModelAttribute("countryName") String countryName, Model model) {
        //todo: error handling
        Result result = getResult(countryName);
        if (result == null) return "redirect:/leagues/error";

        model.addAttribute("countriesList", result.countries());
        model.addAttribute("leaguesList", result.allSelectedLeagues());

        return "leagues";
    }


    //TODO:/leagues/add authority
    @GetMapping("/add")
    public String getLeaguesByCountry(@ModelAttribute("countryName") String countryName, Model model) {
        //todo: error handling
        List<String> countries = this.countryService.getAllCountriesNames();
        List<AddLeagueDto> leagues = new ArrayList<>();

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
        List<AddLeagueDto> selectedLeagues = this.leagueService.getLeaguesByIds(leagueIds);
        model.addAttribute("selectedLeagues", selectedLeagues);
        return "preview-leagues";
    }

    @PostMapping("/saveSelected")
    public String saveSelectedLeagues(@RequestParam List<Long> leagueIds) {
        //todo: error handling
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
        League leagueById = this.leagueService.getLeagueById(leagueId);
        leagueById.setSelected(false);
        this.leagueService.saveLeague(leagueById);
        return "redirect:/leagues/remove";
    }

    private Result getResult(String countryName) {
        List<String> countries = this.countryService.getAllCountriesNames();

        if (this.leagueService.isEmpty()) {
            return null;
        }

        List<LeaguesPageDto> allSelectedLeagues = countryName.equals("all countries") || countryName.isEmpty() ?
                this.leagueService.getAllSelectedLeagues() :
                this.leagueService.getAllSelectedLeaguesByCountry(countryName, false);
        return new Result(countries, allSelectedLeagues);
    }

    private record Result(List<String> countries, List<LeaguesPageDto> allSelectedLeagues) {
    }
}
