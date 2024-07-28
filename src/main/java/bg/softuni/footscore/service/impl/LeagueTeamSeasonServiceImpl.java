package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.repository.LeagueTeamSeasonRepository;
import bg.softuni.footscore.service.LeagueTeamSeasonService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueTeamSeasonServiceImpl implements LeagueTeamSeasonService {
    private final LeagueTeamSeasonRepository seasonLeagueTeamRepository;

    public LeagueTeamSeasonServiceImpl(LeagueTeamSeasonRepository seasonLeagueTeamRepository) {
        this.seasonLeagueTeamRepository = seasonLeagueTeamRepository;
    }

    @Override
    public List<LeagueTeamSeason> getAllByLeagueIdAndSeasonId(long leagueId, long seasonId) {
        return this.seasonLeagueTeamRepository.findByLeagueIdAndSeasonId(leagueId, seasonId);
    }

    @Override
    public List<LeagueTeamSeason> getByLeagueIdAndSeasonId(long leagueId, long seasonId) {
        return this.seasonLeagueTeamRepository.findByLeagueIdAndSeasonId(leagueId, seasonId);
    }

    @Override
    public List<LeagueTeamSeason> getByTeamIdAndLeagueId(long teamId, long leagueId) {
        return this.seasonLeagueTeamRepository.findByTeamIdAndLeagueId(teamId, leagueId);
    }

    @Override
    public List<LeagueTeamSeason> getByTeamIdAndSeasonId(long teamId, long seasonId) {
        return this.seasonLeagueTeamRepository.findByTeamIdAndSeasonId(teamId, seasonId);
    }

    @Override
    @Transactional
    public void save(LeagueTeamSeason seasonLeagueTeam) {
        this.seasonLeagueTeamRepository.save(seasonLeagueTeam);
    }
}
