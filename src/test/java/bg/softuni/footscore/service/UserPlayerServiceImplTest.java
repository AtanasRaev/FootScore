package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.service.impl.UserPlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserPlayerServiceImplTest {

    @Mock
    private RestClient userPlayerRestClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserPlayerServiceImpl userPlayerServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
}
