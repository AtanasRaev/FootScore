package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.UserPlayerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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
                .uri("/my-player")
                .body(userPlayerDto)
                .retrieve()
                .toBodilessEntity();
    }
}
