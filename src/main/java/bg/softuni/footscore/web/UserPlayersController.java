package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.UserPlayerService;
import bg.softuni.footscore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        this.userPlayerService.createPlayer(userPlayerDto);
        return "redirect:/profile";
    }

    @GetMapping("/all-created-players")
    public String showAllPlayers(Model model) {
        model.addAttribute("myPlayers", this.userPlayerService.getAllPlayers());
        model.addAttribute("user", this.userService.getUser());
        return "all-created-players";
    }

    @GetMapping("/profile/my-players")
    public String showMyPlayers(Model model) {
        model.addAttribute("myPlayers", this.userPlayerService.getUserPlayers(this.userService.getUser().getId()));
        return "user-created-players";
    }

    //TODO:add logic for validating IDs
    @DeleteMapping("/my-player/{playerId}/delete")
    public String deleteMyPlayer(@PathVariable Long playerId) {
        this.userPlayerService.deleteMyPlayer(playerId);
        return "redirect:/profile/my-players";
    }

    //TODO:add logic for validating IDs
    @GetMapping("/edit/my-player/{id}")
    public String showEditPlayer(@PathVariable("id") Long id, Model model) {
        UserPlayerDto userPlayer = userPlayerService.getUserPlayerById(id);
        if (userPlayer != null) {
            model.addAttribute("userPlayerData", userPlayer);
            model.addAttribute("positions", POSITIONS);
            return "my-player-edit";
        }
        return "redirect:/error";
    }

    //TODO:add logic for validating IDs
    @PutMapping("/edit/my-player/{id}")
    public String updatePlayer(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("userPlayerData") UserPlayerDto userPlayerDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPlayerData", bindingResult);
            redirectAttributes.addFlashAttribute("userPlayerData", userPlayerDto);
            return "redirect:/edit/" + id;
        }

        this.userPlayerService.updateUserPlayerById(id, userPlayerDto);
        return "redirect:/profile/my-players";
    }
}
