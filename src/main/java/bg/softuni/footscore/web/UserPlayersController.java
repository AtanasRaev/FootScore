package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.UserPlayerService;
import bg.softuni.footscore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserPlayersController {
    private final UserPlayerService userPlayerService;
    private final UserService userService;
    private static final String[] POSITIONS = {"Goalkeeper", "Defender", "Midfielder", "Attacker"};

    public UserPlayersController(UserPlayerService userPlayerService, UserService userService) {
        this.userPlayerService = userPlayerService;
        this.userService = userService;
    }

    @GetMapping("/create/my-player")
    public String showCreatePlayer(Model model) {
        if (!model.containsAttribute("userPlayerData")) {
            model.addAttribute("userPlayerData", new UserPlayerDto());
        }
        model.addAttribute("positions", POSITIONS);
        return "create-player";
    }

    //TODO: fix validation
    @PostMapping("/create/my-player")
    public String doCreatePlayer(@Valid UserPlayerDto userPlayerDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        userPlayerDto.setUserId(this.userService.getUser().getId());
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPlayerData", bindingResult);
            redirectAttributes.addFlashAttribute("userPlayerData", userPlayerDto);
            return "redirect:/my-player";
        }

        userPlayerService.createPlayer(userPlayerDto);
        return "redirect:/profile";
    }

    @GetMapping("/all-created-players")
    public String showAllPlayers(Model model) {
        model.addAttribute("myPlayers", this.userPlayerService.getAllPlayers());
        return "all-created-players";
    }

    @GetMapping("/profile/my-players")
    public String showMyPlayers(Model model) {
        model.addAttribute("myPlayers", this.userPlayerService.getUserPlayers(this.userService.getUser().getId()));
        return "user-created-players";
    }
}
