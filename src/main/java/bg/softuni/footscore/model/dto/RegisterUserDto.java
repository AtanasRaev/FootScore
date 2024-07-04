package bg.softuni.footscore.model.dto;

import jakarta.validation.constraints.*;

public class RegisterUserDto {
    @Size(min = 3, max = 20)
    private String firstName;

    @Size(min = 3, max = 20)
    private String lastName;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String username;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String password;

    @NotEmpty
    @Size(min = 3, max = 20)
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
