package bg.softuni.footscore.model.dto.userDto;

import bg.softuni.footscore.validation.annotations.PasswordMatch;
import bg.softuni.footscore.validation.annotations.UniqueEmail;
import bg.softuni.footscore.validation.annotations.UniqueUsername;
import jakarta.validation.constraints.*;

@PasswordMatch
public class RegisterUserDto {
    @NotNull
    @Size(min = 3, message = "{user.full-name.length}")
    private String firstName;

    @NotNull
    @Size(min = 3, message = "{user.full-name.length}")
    private String lastName;

    @NotNull
    @UniqueEmail
    @Email(regexp = ".+[@].+", message = "{user.email}")
    private String email;

    @NotNull
    @UniqueUsername
    @Size(min = 3, message = "{user.username.length}")
    private String username;

    @Size(min = 3, message = "{user.password.length}")
    private String password;

    @Size(min = 3, message = "{user.confirm-password.length}")
    private String confirmPassword;

    public RegisterUserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public @NotEmpty @Size(min = 2, max = 20) String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty @Size(min = 2, max = 20) String username) {
        this.username = username;
    }
}
