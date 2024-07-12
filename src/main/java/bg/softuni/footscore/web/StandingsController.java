package bg.softuni.footscore.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StandingsController {
    @GetMapping("/widgets/standings")
    public String standings(Model model) {
        return "standings";
    }
}
