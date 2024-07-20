package bg.softuni.footscore.service.impl;

import bg.softuni.footscore.model.entity.SeasonLeagueTeam;
import bg.softuni.footscore.model.entity.Team;
import bg.softuni.footscore.repository.SeasonLeagueTeamRepository;
import bg.softuni.footscore.service.SeasonLeagueTeamService;
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
    public Optional<Team> getTeamByLeagueIdAndSeasonId(long leagueId, long seasonId, long teamId) {
        return this.seasonLeagueTeamRepository.findTeamByLeagueIdAndSeasonId(leagueId, seasonId, teamId);
    }

    @Override
    public void save(SeasonLeagueTeam seasonLeagueTeam) {
        this.seasonLeagueTeamRepository.save(seasonLeagueTeam);
    }
}
