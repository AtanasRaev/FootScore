package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        Optional<UserEntity> optionalUser = userService.getUserByUsername(username);
        optionalUser.ifPresent(userEntity -> model.addAttribute("user", userEntity));
        return "profile";
    }

    @GetMapping("/show-all")
    public String showAll(@RequestParam(required = false, defaultValue = "Teams") String filter,
                          Model model) {
        String[] favorites = {"Teams", "Players"};
        model.addAttribute("favorites", favorites);
        model.addAttribute("selectedFilter", filter);

        Optional<UserEntity> optionalUser = userService.getUser();
        optionalUser.ifPresent(userEntity -> {
            if ("Teams".equals(filter)) {
                model.addAttribute("collection", userEntity.getFavoriteTeams().stream().sorted(Comparator.comparing(Team::getName)));
            } else {
                model.addAttribute("collection", userEntity.getFavoritePlayers().stream().sorted(Comparator.comparing(Player::getShortName)));
            }
        });

        return "all-favourites";
    }
}
