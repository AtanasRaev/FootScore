package bg.softuni.footscore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FixturesController {

    @GetMapping("/widgets/fixtures")
    public String fixtures() {
        return "fixtures";
    }
}
