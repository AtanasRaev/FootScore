package bg.softuni.footscore.service;

import bg.softuni.footscore.model.dto.PlayerTeamSeasonPageDto;
import bg.softuni.footscore.model.entity.PlayerTeamSeason;

import java.util.List;

public interface PlayerTeamSeasonService {
    List<PlayerTeamSeasonPageDto> getByTeamIdAndSeasonId(Long teamId, Long seasonId);

    void save(PlayerTeamSeason seasonTeamPlayer);

}
