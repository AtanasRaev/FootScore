package bg.softuni.footscore.model.dto.userDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginUserDto {
    @NotEmpty
    @Size(min = 3, max = 20)
    private String username;
    @NotEmpty
    @Size(min = 3, max = 20)
    private String password;

    public LoginUserDto() {
    }

    public @NotEmpty @Size(min = 3, max = 20) String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty @Size(min = 3, max = 20) String username) {
        this.username = username;
    }

    public @NotEmpty @Size(min = 3, max = 20) String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty @Size(min = 3, max = 20) String password) {
        this.password = password;
    }
}
