package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("registerData") RegisterUserDto registerData) {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid RegisterUserDto registerData) {
        this.userService.registerUser(registerData);
        return "redirect:/login";
    }
}
