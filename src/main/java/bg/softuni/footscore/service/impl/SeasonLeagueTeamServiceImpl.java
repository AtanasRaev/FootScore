package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.SeasonLeagueTeam;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.repository.SeasonLeagueTeamRepository;
import bg.softuni.footscore.service.SeasonLeagueTeamService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonLeagueTeamServiceImpl implements SeasonLeagueTeamService {
    private final SeasonLeagueTeamRepository seasonLeagueTeamRepository;

    public SeasonLeagueTeamServiceImpl(SeasonLeagueTeamRepository seasonLeagueTeamRepository) {
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
    public List<Optional<SeasonLeagueTeam>> getTeamsByLeagueIdAndSeasonId(long leagueId, long seasonId) {
        return this.seasonLeagueTeamRepository.findTeamByLeagueIdAndSeasonId(leagueId, seasonId);
    }

    @Override
    public Optional<SeasonLeagueTeam> getByTeamIdAndLeagueId(long teamId, long leagueId) {
        return this.seasonLeagueTeamRepository.findByTeamIdAndLeagueId(teamId, leagueId);
    }

    @Override
    public List<SeasonLeagueTeam> getByTeamIdAndSeasonId(long teamId, long seasonId) {
        return this.seasonLeagueTeamRepository.findByTeamIdAndSeasonId(teamId, seasonId);
    }

    @Override
    @Transactional
    public void save(SeasonLeagueTeam seasonLeagueTeam) {
        this.seasonLeagueTeamRepository.save(seasonLeagueTeam);
    }
}
