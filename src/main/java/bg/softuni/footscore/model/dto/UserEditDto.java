package bg.softuni.footscore.model.dto;

import bg.softuni.footscore.validation.annotations.UniqueUsername;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserEditDto {
    @NotNull
    @UniqueUsername
    @Size(min = 3, message = "{user.username.length}")
    private String username;

    public @NotNull @Size(min = 3, message = "{user.username.length}") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull @Size(min = 3, message = "{user.username.length}") String username) {
        this.username = username;
    }
}
