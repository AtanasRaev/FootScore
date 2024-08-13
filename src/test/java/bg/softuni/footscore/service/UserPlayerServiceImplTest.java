package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.impl.UserPlayerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPlayerServiceImplTest {

    @Mock
    private RestClient userPlayerRestClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserPlayerServiceImpl userPlayerServiceImpl;

    @Test
    void testGetAllPlayers() {
        List<UserPlayerDto> expectedPlayers = List.of(new UserPlayerDto(), new UserPlayerDto());

        doReturn(requestHeadersUriSpec).when(userPlayerRestClient).get();
        doReturn(requestBodySpec).when(requestHeadersUriSpec).uri("/my-player/show-all");
        doReturn(requestBodySpec).when(requestBodySpec).accept(MediaType.APPLICATION_JSON);
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(expectedPlayers).when(responseSpec).body(new ParameterizedTypeReference<List<UserPlayerDto>>() {
        });

        List<UserPlayerDto> actualPlayers = userPlayerServiceImpl.getAllPlayers();

        verify(userPlayerRestClient).get();
        verify(requestHeadersUriSpec).uri("/my-player/show-all");
        verify(requestBodySpec).accept(MediaType.APPLICATION_JSON);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(new ParameterizedTypeReference<List<UserPlayerDto>>() {
        });

        assertEquals(expectedPlayers, actualPlayers);
    }

    @Test
    void testCreatePlayer() {
        UserPlayerDto userPlayerDto = new UserPlayerDto();

        when(userPlayerRestClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/my-player/create")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(userPlayerDto)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(null);

        userPlayerServiceImpl.createPlayer(userPlayerDto);

        verify(userPlayerRestClient).post();
        verify(requestBodyUriSpec).uri("/my-player/create");
        verify(requestBodySpec).body(userPlayerDto);
    }

    @Test
    void testGetUserPlayers() {
        Long userId = 1L;
        List<UserPlayerDto> expectedPlayers = List.of(new UserPlayerDto(), new UserPlayerDto());

        doReturn(requestHeadersUriSpec).when(userPlayerRestClient).get();
        doReturn(requestBodySpec).when(requestHeadersUriSpec).uri("/my-player/user-players/{id}", userId);
        doReturn(requestBodySpec).when(requestBodySpec).accept(MediaType.APPLICATION_JSON);
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(expectedPlayers).when(responseSpec).body(new ParameterizedTypeReference<List<UserPlayerDto>>() {
        });

        List<UserPlayerDto> actualPlayers = userPlayerServiceImpl.getUserPlayers(userId);

        verify(userPlayerRestClient).get();
        verify(requestHeadersUriSpec).uri("/my-player/user-players/{id}", userId);
        verify(requestBodySpec).accept(MediaType.APPLICATION_JSON);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(new ParameterizedTypeReference<List<UserPlayerDto>>() {
        });

        assertEquals(expectedPlayers, actualPlayers);
    }

    @Test
    void testDeleteMyPlayer() {
        Long playerId = 1L;

        doReturn(requestHeadersUriSpec).when(userPlayerRestClient).delete();
        doReturn(requestBodySpec).when(requestHeadersUriSpec).uri("/my-player/delete/{id}", playerId);
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(null).when(responseSpec).toBodilessEntity();

        userPlayerServiceImpl.deleteMyPlayer(playerId);

        verify(userPlayerRestClient).delete();
        verify(requestHeadersUriSpec).uri("/my-player/delete/{id}", playerId);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).toBodilessEntity();
    }

    @Test
    void testGetUserPlayerById() {
        Long playerId = 1L;
        UserPlayerDto expectedPlayer = new UserPlayerDto();

        doReturn(requestHeadersUriSpec).when(userPlayerRestClient).get();
        doReturn(requestBodySpec).when(requestHeadersUriSpec).uri("/my-player/{id}", playerId);
        doReturn(requestBodySpec).when(requestBodySpec).accept(MediaType.APPLICATION_JSON);
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(expectedPlayer).when(responseSpec).body(UserPlayerDto.class);

        UserPlayerDto actualPlayer = userPlayerServiceImpl.getUserPlayerById(playerId);

        verify(userPlayerRestClient).get();
        verify(requestHeadersUriSpec).uri("/my-player/{id}", playerId);
        verify(requestBodySpec).accept(MediaType.APPLICATION_JSON);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(UserPlayerDto.class);

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    void testUpdateUserPlayerById() {
        Long playerId = 1L;
        UserPlayerDto userPlayerDto = new UserPlayerDto();

        doReturn(requestBodyUriSpec).when(userPlayerRestClient).put();
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri("/my-player/update/{id}", playerId);
        doReturn(requestBodySpec).when(requestBodySpec).body(userPlayerDto);
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        doReturn(null).when(responseSpec).toBodilessEntity();

        userPlayerServiceImpl.updateUserPlayerById(playerId, userPlayerDto);

        verify(userPlayerRestClient).put();
        verify(requestBodyUriSpec).uri("/my-player/update/{id}", playerId);
        verify(requestBodySpec).body(userPlayerDto);
        verify(requestBodySpec).retrieve();
        verify(responseSpec).toBodilessEntity();
    }
}
