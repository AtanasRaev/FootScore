package bg.softuni.footscore.userplayer.web;

import bg.softuni.footscore.userplayer.model.dto.CreateUserPlayerDto;
import bg.softuni.footscore.userplayer.model.dto.UserPlayerDto;
import bg.softuni.footscore.userplayer.service.UserPlayerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my-player")
@CrossOrigin(origins = "http://localhost:8080")
public class PlayerController {
    private final UserPlayerService userPlayerService;


    public PlayerController(UserPlayerService userPlayerService) {
        this.userPlayerService = userPlayerService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserPlayerDto> createPlayer(@RequestBody @Valid CreateUserPlayerDto createUserPlayerDto) {
        this.userPlayerService.addUserPlayer(createUserPlayerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/show-all")
    public ResponseEntity<List<UserPlayerDto>> getAllPlayers () {
        return ResponseEntity.ok(
                this.userPlayerService.getAllUserPlayers()
        );
    }

    @GetMapping("/user-players/{id}")
    public ResponseEntity<List<UserPlayerDto>> getAllUserPlayers (@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                this.userPlayerService.getAllUserPlayersById(id)
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserPlayerDto> deletePlayerById(@PathVariable("id") Long id) {
        this.userPlayerService.deleteUserPlayerById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPlayerDto> findPlayerById(@PathVariable("id") Long id) {
        return ResponseEntity
                .ok(this.userPlayerService.getUserPlayerById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserPlayerDto> updatePlayerById(
            @PathVariable("id") Long id,
            @RequestBody @Valid UserPlayerDto userPlayerDto) {

        UserPlayerDto updatedUserPlayer = this.userPlayerService.updateUserPlayerById(id, userPlayerDto);

        if (updatedUserPlayer != null) {
            return ResponseEntity.ok(updatedUserPlayer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
