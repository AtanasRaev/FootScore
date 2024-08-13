package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.userDto.RolePageDto;
import bg.softuni.footscore.model.dto.userDto.UserEntityPageDto;
import bg.softuni.footscore.model.dto.userDto.UserPlayerDto;
import bg.softuni.footscore.model.enums.RoleEnum;
import bg.softuni.footscore.service.UserPlayerService;
import bg.softuni.footscore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserPlayerControllerTest {

    @Mock
    private UserPlayerService userPlayerService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private UserPlayerController userPlayerController;

    private static final String[] POSITIONS = {"Goalkeeper", "Defender", "Midfielder", "Attacker"};

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserEntityPageDto createMockUser(Long id, RoleEnum role) {
        RolePageDto rolePageDto = new RolePageDto();
        rolePageDto.setRole(role);

        UserEntityPageDto user = new UserEntityPageDto();
        user.setId(id);
        user.setRole(rolePageDto);

        return user;
    }

    private UserPlayerDto createMockPlayer(Long id, Long userId) {
        UserPlayerDto player = new UserPlayerDto();
        player.setUserId(userId);
        return player;
    }

    @Test
    void testShowCreatePlayer() {
        String viewName = userPlayerController.showCreatePlayer(model);

        verify(model).addAttribute(eq("userPlayerData"), any(UserPlayerDto.class));
        verify(model).addAttribute("positions", POSITIONS);
        assertEquals("create-player", viewName);
    }

    @Test
    void testDoCreatePlayerWithErrors() {
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        when(userService.getUser()).thenReturn(mockUser);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userPlayerController.doCreatePlayer(new UserPlayerDto(), bindingResult, redirectAttributes, model);

        verify(redirectAttributes).addFlashAttribute(eq("org.springframework.validation.BindingResult.userPlayerData"), eq(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq("userPlayerData"), any(UserPlayerDto.class));
        assertEquals("redirect:/create/my-player", viewName);
    }

    @Test
    void testDoCreatePlayerWithoutErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        when(userService.getUser()).thenReturn(mockUser);

        UserPlayerDto userPlayerDto = new UserPlayerDto();
        userPlayerDto.setUserId(1L);

        String viewName = userPlayerController.doCreatePlayer(userPlayerDto, bindingResult, redirectAttributes, model);

        verify(userPlayerService).createPlayer(userPlayerDto);
        assertEquals("redirect:/profile", viewName);
    }

    @Test
    void testShowMyPlayers() {
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        when(userService.getUser()).thenReturn(mockUser);

        List<UserPlayerDto> mockPlayers = new ArrayList<>();
        when(userPlayerService.getUserPlayers(1L)).thenReturn(mockPlayers);

        String viewName = userPlayerController.showMyPlayers(model);

        verify(userService).getUser();
        verify(userPlayerService).getUserPlayers(1L);
        verify(model).addAttribute("myPlayers", mockPlayers);
        assertEquals("user-created-players", viewName);
    }

    @Test
    void testShowAllPlayers() {
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        when(userService.getUser()).thenReturn(mockUser);

        String viewName = userPlayerController.showAllPlayers(model);

        verify(model).addAttribute(eq("myPlayers"), any());
        verify(model).addAttribute(eq("user"), eq(mockUser));
        assertEquals("all-created-players", viewName);
    }

    @Test
    void testDeleteMyPlayer() throws AccessDeniedException {
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        UserPlayerDto mockPlayer = createMockPlayer(1L, 1L);

        when(userService.getUser()).thenReturn(mockUser);
        when(userPlayerService.getUserPlayerById(1L)).thenReturn(mockPlayer);

        String viewName = userPlayerController.deleteMyPlayer(1L);

        verify(userPlayerService).deleteMyPlayer(1L);
        assertEquals("redirect:/profile/my-players", viewName);
    }

    @Test
    void testDeleteMyPlayerAccessDenied() {
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        UserPlayerDto mockPlayer = createMockPlayer(1L, 2L);

        when(userService.getUser()).thenReturn(mockUser);
        when(userPlayerService.getUserPlayerById(1L)).thenReturn(mockPlayer);

        assertThrows(AccessDeniedException.class, () -> userPlayerController.deleteMyPlayer(1L));
    }

    @Test
    void testShowEditPlayer() throws AccessDeniedException {
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        UserPlayerDto mockPlayer = createMockPlayer(1L, 1L);

        when(userService.getUser()).thenReturn(mockUser);
        when(userPlayerService.getUserPlayerById(1L)).thenReturn(mockPlayer);

        String viewName = userPlayerController.showEditPlayer(1L, model);

        verify(model).addAttribute("userPlayerData", mockPlayer);
        verify(model).addAttribute("positions", POSITIONS);
        assertEquals("my-player-edit", viewName);
    }

    @Test
    void testShowEditPlayerAccessDenied() {
        UserEntityPageDto mockUser = createMockUser(1L, RoleEnum.USER);
        UserPlayerDto mockPlayer = createMockPlayer(2L, 2L);

        when(userService.getUser()).thenReturn(mockUser);
        when(userPlayerService.getUserPlayerById(1L)).thenReturn(mockPlayer);

        assertThrows(AccessDeniedException.class, () -> userPlayerController.showEditPlayer(1L, model));
    }

    @Test
    void testUpdatePlayerWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userPlayerController.updatePlayer(1L, new UserPlayerDto(), bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("org.springframework.validation.BindingResult.userPlayerData"), eq(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq("userPlayerData"), any(UserPlayerDto.class));
        assertEquals("redirect:/edit/1", viewName);
    }

    @Test
    void testUpdatePlayerWithoutErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = userPlayerController.updatePlayer(1L, new UserPlayerDto(), bindingResult, redirectAttributes);

        verify(userPlayerService).updateUserPlayerById(eq(1L), any(UserPlayerDto.class));
        assertEquals("redirect:/profile/my-players", viewName);
    }
}
