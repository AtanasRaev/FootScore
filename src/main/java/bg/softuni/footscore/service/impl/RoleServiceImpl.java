package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.repository.RoleRepository;
import bg.softuni.footscore.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getAdminRole() {
        return this.roleRepository.findById(1L).isPresent() ? this.roleRepository.findById(1L).get() : null;
    }

    @Override
    public Role getUserRole() {
        return this.roleRepository.findById(2L).isPresent() ? this.roleRepository.findById(2L).get() : null;
    }
}
