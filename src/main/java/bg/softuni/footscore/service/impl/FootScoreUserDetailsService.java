package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.UserEntity;
import bg.softuni.footscore.repository.UserEntityRepository;
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
        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of())
                .disabled(false)
                .build();
    }
}
