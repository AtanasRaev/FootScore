package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.UserPlayerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class UserPlayerServiceImpl implements UserPlayerService {
    private final RestClient userPlayerRestClient;

    public UserPlayerServiceImpl(@Qualifier("userPlayerRestClient") RestClient userPlayerRestClient) {
        this.userPlayerRestClient = userPlayerRestClient;
    }

    @Override
    public void createPlayer(UserPlayerDto userPlayerDto) {
        this.userPlayerRestClient
                .post()
                .uri("/my-player/create")
                .body(userPlayerDto)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public List<UserPlayerDto> getAllPlayers() {
        return userPlayerRestClient
                .get()
                .uri("/my-player/show-all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    @Override
    public List<UserPlayerDto> getUserPlayers(Long id) {
        return userPlayerRestClient
                .get()
                .uri("/my-player/user-players/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }
}
