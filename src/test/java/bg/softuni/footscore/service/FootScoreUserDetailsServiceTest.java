package bg.softuni.footscore.service;

import bg.softuni.footscore.model.entity.Role;
import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.model.enums.RoleEnum;
import bg.softuni.footscore.model.user.FootScoreUserDetails;
import bg.softuni.footscore.repository.UserEntityRepository;
import bg.softuni.footscore.service.impl.FootScoreUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FootScoreUserDetailsServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private FootScoreUserDetailsService footScoreUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_validUser_returnsUserDetails() {

        Role userRole = new Role();
        userRole.setRole(RoleEnum.USER);

        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("password");
        userEntity.setRole(userRole);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = footScoreUserDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertEquals("John", ((FootScoreUserDetails) userDetails).getFirstName());
        assertEquals("Doe", ((FootScoreUserDetails) userDetails).getLastName());
    }

    @Test
    void loadUserByUsername_invalidUser_throwsUsernameNotFoundException() {
        String username = "invaliduser";
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> footScoreUserDetailsService.loadUserByUsername(username),
                "Expected loadUserByUsername() to throw, but it didn't"
        );

        assertEquals("User with username " + username + "not found!", thrown.getMessage());
    }
}
