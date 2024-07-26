package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.repository.PlayerTeamSeasonRepository;
import bg.softuni.footscore.service.PlayerTeamSeasonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerTeamSeasonServiceImpl implements PlayerTeamSeasonService {
    private final PlayerTeamSeasonRepository seasonTeamPlayerRepository;

    public PlayerTeamSeasonServiceImpl(PlayerTeamSeasonRepository seasonTeamPlayerRepository) {
        this.seasonTeamPlayerRepository = seasonTeamPlayerRepository;
    }

    @Override
    public List<Player> getAllPlayersBySeasonIdAndTeamId(long teamId, long seasonId) {
        return this.seasonTeamPlayerRepository.findPlayersByTeamIdAndSeasonId(teamId, seasonId);
    }

    @Override
    public Optional<Player> getPlayerByTeamIdAndSeasonId(long teamId, long seasonId, long playerId) {
        return this.seasonTeamPlayerRepository.findPlayerByTeamIdAndSeasonId(teamId, seasonId, playerId);
    }

    @Override
    public void save(bg.softuni.footscore.model.entity.PlayerTeamSeason seasonTeamPlayer) {
        this.seasonTeamPlayerRepository.save(seasonTeamPlayer);
    }
}
