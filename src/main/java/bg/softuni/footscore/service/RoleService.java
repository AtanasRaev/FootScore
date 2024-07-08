package bg.softuni.footscore.service;

import bg.softuni.footscore.model.entity.Role;

public interface RoleService {
    Role getAdminRole();

    Role getUserRole();
}
