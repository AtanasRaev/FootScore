package bg.softuni.footscore.web;

import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String upload(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        Optional<UserEntity> optionalUser = userService.getUserByUsername(username);
        optionalUser.ifPresent(userEntity -> model.addAttribute("user", userEntity));

        return "profile";
    }

    @GetMapping("/show-more")
    public String showMore() {
        return "all-favourites";
    }
}
