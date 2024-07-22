package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.Player;
import bg.softuni.footscore.model.entity.SeasonTeamPlayer;
import bg.softuni.footscore.repository.SeasonTeamPlayerRepository;
import bg.softuni.footscore.service.SeasonTeamPlayerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonTeamPlayerServiceImpl implements SeasonTeamPlayerService {
    private final SeasonTeamPlayerRepository seasonTeamPlayerRepository;

    public SeasonTeamPlayerServiceImpl(SeasonTeamPlayerRepository seasonTeamPlayerRepository) {
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
    public void save(SeasonTeamPlayer seasonTeamPlayer) {
        this.seasonTeamPlayerRepository.save(seasonTeamPlayer);
    }
}
