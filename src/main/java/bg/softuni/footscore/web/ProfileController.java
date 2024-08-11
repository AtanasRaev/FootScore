package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.teamDto.DreamTeamPageDto;
import bg.softuni.footscore.model.dto.userDto.UserEditDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.playerDto.PlayerPageDto;
import bg.softuni.footscore.model.dto.teamDto.TeamPageDto;
import bg.softuni.footscore.service.DreamTeamService;
import bg.softuni.footscore.service.PlayerService;
import bg.softuni.footscore.service.TeamService;
import bg.softuni.footscore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

@Controller
public class ProfileController {
    private final UserService userService;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final DreamTeamService dreamTeamService;
    private static final String[] FAVORITES = {"Teams", "Players"};

    public ProfileController(UserService userService,
                             TeamService teamService,
                             PlayerService playerService,
                             DreamTeamService dreamTeamService) {
        this.userService = userService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.dreamTeamService = dreamTeamService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserEntityPageDto user = userService.getUserByUsername(username);
        List<DreamTeamPageDto> allDreamTeamsByUserId = this.dreamTeamService.getAllDreamTeamsByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("dreamTeams", allDreamTeamsByUserId);
        return "profile";
    }

    @GetMapping("/show-all")
    public String showAll(@RequestParam(required = false, defaultValue = "Teams") String filter,
                          @RequestParam(required = false) String selectedFilter,
                          Model model) {
        if (selectedFilter != null) {
            filter = selectedFilter;
        }
        return getAllFavorite(filter, model, "all-favorites");
    }

    @PostMapping("/show-all")
    public String removeLeague(@RequestParam(required = false, defaultValue = "Teams") String filter,
                               @RequestParam(required = false) List<Long> itemIds,
                               @RequestParam(required = false) String selectedFilter,
                               Model model) {

        if (itemIds == null) {
            return "redirect:/show-all";
        }

        if (selectedFilter != null) {
            filter = selectedFilter;
        }

        UserEntityPageDto user = this.userService.getUser();

        removeFavorites(filter, itemIds, user);

        model.addAttribute("selectedFilter", filter);
        return "redirect:/show-all";
    }


    @GetMapping("/show-all/edit")
    public String showAllEdit(@RequestParam(required = false, defaultValue = "Teams") String filter,
                              @RequestParam(required = false) String selectedFilter,
                              Model model) {
        if (selectedFilter != null) {
            filter = selectedFilter;
        }
        return getAllFavorite(filter, model, "edit-favorites");
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        UserEntityPageDto user = this.userService.getUser();
        model.addAttribute("user", user);

        if (!model.containsAttribute("editData")) {
            model.addAttribute("editData", new UserEditDto());
        }

        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String doEditProfile(@Valid @ModelAttribute("editData") UserEditDto dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editData", bindingResult);
            redirectAttributes.addFlashAttribute("editData", dto);
            return "redirect:/profile/edit";
        }

        this.userService.updateUsername(dto);
        return "redirect:/logout";
    }

    private void removeFavorites(String filter, List<Long> itemIds, UserEntityPageDto user) {
        if ("Teams".equals(filter)) {
            List<TeamPageDto> teamsToRemove = this.teamService.findAllByIds(itemIds);
            this.userService.removeFavoriteTeams(user, teamsToRemove);
        } else {
            List<PlayerPageDto> playersToRemove = this.playerService.getAllByIds(itemIds);
            this.userService.removeFavoritePlayers(user, playersToRemove);
        }
    }

    private String getAllFavorite(String filter, Model model, String template) {
        model.addAttribute("favorites", FAVORITES);
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
        return template;
    }
}
