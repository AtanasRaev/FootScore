package bg.softuni.footscore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeagueController {

    @GetMapping("/leagues")
    public String league() {
        return "leagues";
    }

    @GetMapping("/leagues-info")
    public String get() {
        return "leagues-info";
    }

    @GetMapping("/leagues/add")
    public String addLeague() {
        return "add-leagues";
    }
}
