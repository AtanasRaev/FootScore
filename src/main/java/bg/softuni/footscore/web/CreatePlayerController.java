package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.UserPlayerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CreatePlayerController {
    private final UserPlayerService userPlayerService;
    private static final String[] POSITIONS = {"Goalkeeper", "Defender", "Midfielder", "Attacker"};

    public CreatePlayerController(UserPlayerService userPlayerService) {
        this.userPlayerService = userPlayerService;
    }

    @GetMapping("/my-player")
    public String showCreatePlayer(Model model) {
        if (!model.containsAttribute("userPlayerData")) {
            model.addAttribute("userPlayerData", new UserPlayerDto());
        }
        return "create-player";
    }

    //TODO: fix validation
    @PostMapping("/my-player")
    public String doCreatePlayer(@Valid UserPlayerDto userPlayerDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPlayerData", bindingResult);
            redirectAttributes.addFlashAttribute("userPlayerData", userPlayerDto);
            return "redirect:/my-player";
        }

        userPlayerService.createPlayer(userPlayerDto);
        return "redirect:/profile";
    }
}
