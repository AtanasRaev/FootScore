package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    @ModelAttribute("registerData")
    public RegisterUserDto registerUserDto() {
        return new RegisterUserDto();
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("register")
    public String register(@Valid RegisterUserDto registerData) {

        this.userService.registerUser(registerData);

        return "redirect:/";
    }
}
