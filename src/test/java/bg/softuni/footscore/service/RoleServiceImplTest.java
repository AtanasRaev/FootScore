package bg.softuni.footscore.service;

import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.model.enums.RoleEnum;
import bg.softuni.footscore.repository.RoleRepository;
import bg.softuni.footscore.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role adminRole;
    private Role userRole;

    @BeforeEach
    public void setUp() {
        adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setRole(RoleEnum.ADMIN);

        userRole = new Role();
        userRole.setId(2L);
        userRole.setRole(RoleEnum.USER);
    }

    @Test
    public void testGetAdminRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(adminRole));

        Role result = roleService.getAdminRole();

        assertNotNull(result);
        assertEquals(adminRole.getId(), result.getId());
        assertEquals(adminRole.getRole(), result.getRole());

        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAdminRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Role result = roleService.getAdminRole();

        assertNull(result);

        verify(roleRepository).findById(1L);
    }

    @Test
    public void testGetUserRole() {
        when(roleRepository.findById(2L)).thenReturn(Optional.of(userRole));

        Role result = roleService.getUserRole();

        assertNotNull(result);
        assertEquals(userRole.getId(), result.getId());
        assertEquals(userRole.getRole(), result.getRole());

        verify(roleRepository).findById(2L);
    }

    @Test
    public void testGetUserRoleNotFound() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        Role result = roleService.getUserRole();

        assertNull(result);

        verify(roleRepository).findById(2L);
    }
}
