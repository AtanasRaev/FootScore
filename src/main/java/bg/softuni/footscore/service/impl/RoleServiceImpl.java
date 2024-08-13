package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.repository.RoleRepository;
import bg.softuni.footscore.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getAdminRole() {
        Optional<Role> role = this.roleRepository.findById(1L);
        return role.orElse(null);
    }

    @Override
    public Role getUserRole() {
        Optional<Role> role = this.roleRepository.findById(2L);
        return role.orElse(null);
    }
}
