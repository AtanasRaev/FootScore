package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.model.enums.RoleEnum;
import bg.softuni.footscore.model.user.FootScoreUserDetails;
import bg.softuni.footscore.repository.UserEntityRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class FootScoreUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    public FootScoreUserDetailsService(UserEntityRepository userRepository) {

        this.userEntityRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .map(FootScoreUserDetailsService::map)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + username + "not found!")
                );
    }

    private static UserDetails map(UserEntity user) {
        return new FootScoreUserDetails(
                user.getEmail(),
                user.getPassword(),
                List.of(map(user.getRole().getRole())),
                user.getFirstName(),
                user.getLastName());
    }

    private static GrantedAuthority map(RoleEnum role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }
}
