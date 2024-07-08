package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.AddLeagueDto;
import bg.softuni.footscore.model.dto.ApiLeagueDto;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.impl.CountryServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LeagueController {
    private final LeagueService leagueService;
    private final CountryServiceImpl countryService;

    public LeagueController(LeagueService leagueService, CountryServiceImpl countryService) {
        this.leagueService = leagueService;
        this.countryService = countryService;
    }

    @GetMapping("/leagues")
    public String league() {
        return "leagues";
    }

    @GetMapping("/leagues-info")
    public String get() {
        return "leagues-info";
    }

    @GetMapping("/leagues/add")
    public String getLeaguesByCountry(@ModelAttribute("countryName") String countryName, Model model) {
        List<String> countries = countryService.getAllCountriesNames();
        List<AddLeagueDto> leagues = new ArrayList<>();

        if (countryName != null && !countryName.isEmpty()) {
            leagues = leagueService.getAllLeaguesByCountry(countryName);
        }

        model.addAttribute("countries", countries);
        model.addAttribute("leagues", leagues);
        return "add-leagues";
    }
}
