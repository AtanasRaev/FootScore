package bg.softuni.footscore.model.dto.userDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginUserDto {
    @NotEmpty
    @Size(min = 3, max = 20, message = "{user.username.length}")
    private String username;
    @NotEmpty
    @Size(min = 3, max = 20 , message = "{user.username.password}")
    private String password;

    public LoginUserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
