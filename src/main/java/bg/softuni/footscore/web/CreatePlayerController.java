package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.UserPlayerDto;
import bg.softuni.footscore.service.UserPlayerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CreatePlayerController {
    private final UserPlayerService userPlayerService;
    private static final String[] POSITIONS = {"Goalkeeper", "Defender", "Midfielder", "Attacker"};

    public CreatePlayerController(UserPlayerService userPlayerService) {
        this.userPlayerService = userPlayerService;
    }

    @GetMapping("/my-player")
    public String showCreatePlayer(Model model) {
        model.addAttribute("positions", POSITIONS);
        return "create-player";
    }

    @PostMapping("/my-player")
    public String doCreatePlayer(@Valid UserPlayerDto userPlayerDto) {
        userPlayerService.createPlayer(userPlayerDto);
        return "redirect:/profile";
    }
}
