package bg.softuni.footscore.model.dto.userDto;

import bg.softuni.footscore.model.enums.RoleEnum;

public class RolePageDto {
    private long id;
    private RoleEnum role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
