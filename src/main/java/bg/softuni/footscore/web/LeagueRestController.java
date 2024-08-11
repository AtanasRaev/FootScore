package bg.softuni.footscore.web;

import bg.softuni.footscore.model.dto.leagueDto.LeagueDto;
import bg.softuni.footscore.service.LeagueService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeagueRestController {
    private final LeagueService leagueService;
    private final ModelMapper modelMapper;

    public LeagueRestController(LeagueService leagueService,
                                ModelMapper modelMapper) {
        this.leagueService = leagueService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/api/leagues")
    public ResponseEntity<List<LeagueDto>> getAllPlayers () {
        return ResponseEntity.ok(
                this.leagueService.getAllLeagues().stream().map(l -> this.modelMapper.map(l, LeagueDto.class)).toList()
        );
    }
}
