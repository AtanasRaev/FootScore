package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.dto.LeaguesPageDto;
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
        List<String> countries = this.countryService.getAllCountriesNames();

        if (this.leagueService.isEmpty()) {
            return "redirect:/leagues/error";
        }

        List<LeaguesPageDto> allSelectedLeagues = countryName.equals("all countries") || countryName.isEmpty() ?
                this.leagueService.getAllSelectedLeagues() :
                this.leagueService.getAllSelectedLeaguesByCountry(countryName, false);
        model.addAttribute("countriesList", countries);
        model.addAttribute("leaguesList", allSelectedLeagues);

        return "leagues";
    }



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
}
