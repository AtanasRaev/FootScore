package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.repository.LeagueTeamSeasonRepository;
import bg.softuni.footscore.service.LeagueTeamSeasonService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeagueTeamSeasonServiceImpl implements LeagueTeamSeasonService {
    private final LeagueTeamSeasonRepository seasonLeagueTeamRepository;

    public LeagueTeamSeasonServiceImpl(LeagueTeamSeasonRepository seasonLeagueTeamRepository) {
        this.seasonLeagueTeamRepository = seasonLeagueTeamRepository;
    }

    @Override
    public List<Team> getAllTeamsBySeasonIdAndLeagueId(long leagueId, long seasonId) {
        return this.seasonLeagueTeamRepository.findTeamsByLeagueIdAndSeasonId(leagueId, seasonId);
    }

    @Override
    public Optional<Team> getTeamByLeagueIdAndSeasonIdAndTeamId(long leagueId, long seasonId, long teamId) {
        return this.seasonLeagueTeamRepository.findTeamByLeagueIdAndSeasonIdAndTeamId(leagueId, seasonId, teamId);
    }

    @Override
    public List<Optional<LeagueTeamSeason>> getTeamsByLeagueIdAndSeasonId(long leagueId, long seasonId) {
        return this.seasonLeagueTeamRepository.findTeamByLeagueIdAndSeasonId(leagueId, seasonId);
    }

    @Override
    public Optional<LeagueTeamSeason> getByTeamIdAndLeagueId(long teamId, long leagueId) {
        return this.seasonLeagueTeamRepository.findByTeamIdAndLeagueId(teamId, leagueId);
    }

    @Override
    public List<LeagueTeamSeason> getByTeamIdAndSeasonId(long teamId, long seasonId) {
        return this.seasonLeagueTeamRepository.findByTeamIdAndSeasonId(teamId, seasonId);
    }

    @Override
    public List<LeagueTeamSeason> getByLeagueIdAndSeasonId(long leagueId, long seasonId) {
        return this.seasonLeagueTeamRepository.findByLeagueIdAndSeasonId(leagueId, seasonId);
    }

    @Override
    @Transactional
    public void save(LeagueTeamSeason seasonLeagueTeam) {
        this.seasonLeagueTeamRepository.save(seasonLeagueTeam);
    }
}
