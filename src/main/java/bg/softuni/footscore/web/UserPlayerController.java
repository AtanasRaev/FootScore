package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.UserPlayerService;
import bg.softuni.footscore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@Controller
public class UserPlayerController {
    private final UserPlayerService userPlayerService;
    private final UserService userService;
    private static final String[] POSITIONS = {"Goalkeeper", "Defender", "Midfielder", "Attacker"};

    public UserPlayerController(UserPlayerService userPlayerService, UserService userService) {
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

    @PostMapping("/create/my-player")
    public String doCreatePlayer(@Valid UserPlayerDto userPlayerDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        userPlayerDto.setUserId(this.userService.getUser().getId());
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPlayerData", bindingResult);
            redirectAttributes.addFlashAttribute("userPlayerData", userPlayerDto);
            return "redirect:/create/my-player";
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

    @DeleteMapping("/my-player/{playerId}/delete")
    public String deleteMyPlayer(@PathVariable Long playerId) throws AccessDeniedException {
        UserEntityPageDto user = this.userService.getUser();
        UserPlayerDto userPlayer = userPlayerService.getUserPlayerById(playerId);

        if (user.getRole().getRole().toString().equals("USER") && user.getId() != userPlayer.getUserId()) {
            throw new AccessDeniedException("You do not have permission to access this page.");
        }

        this.userPlayerService.deleteMyPlayer(playerId);
        return "redirect:/profile/my-players";
    }

    @GetMapping("/edit/my-player/{id}")
    public String showEditPlayer(@PathVariable("id") Long id, Model model) throws AccessDeniedException {
        UserEntityPageDto user = this.userService.getUser();
        UserPlayerDto userPlayer = userPlayerService.getUserPlayerById(id);

        if (user.getId() != userPlayer.getUserId()) {
            throw new AccessDeniedException("You do not have permission to access this page.");
        }

        model.addAttribute("userPlayerData", userPlayer);
        model.addAttribute("positions", POSITIONS);
        return "my-player-edit";
    }

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
