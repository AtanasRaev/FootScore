package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;

@Controller
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserEntityPageDto user = userService.getUserByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "profile";
    }

    @GetMapping("/show-all")
    public String showAll(@RequestParam(required = false, defaultValue = "Teams") String filter,
                          Model model) {
        String[] favorites = {"Teams", "Players"};
        model.addAttribute("favorites", favorites);
        model.addAttribute("selectedFilter", filter);

        UserEntityPageDto user = userService.getUser();
        if (user != null) {
            if ("Teams".equals(filter)) {
                model.addAttribute("collection", user.getFavoriteTeams()
                        .stream()
                        .sorted(Comparator.comparing(TeamPageDto::getName)));
            } else {
                model.addAttribute("collection", user.getFavoritePlayers()
                        .stream()
                        .sorted(Comparator.comparing(PlayerPageDto::getShortName)));
            }
        }

        return "all-favourites";
    }
}
