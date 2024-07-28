package bg.softuni.footscore.utils;

import bg.softuni.footscore.model.dto.SeasonPageDto;
import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.service.LeagueTeamSeasonService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SeasonUtils {

    public static Set<SeasonPageDto> getCurrentSeasonsForLeague(long leagueId, LeagueTeamSeasonService leagueTeamSeasonService, List<SeasonPageDto> allSeasons) {
        Set<SeasonPageDto> currentSeasons = new LinkedHashSet<>();
        for (SeasonPageDto season : allSeasons) {
            List<LeagueTeamSeason> leagueTeamSeasons = leagueTeamSeasonService.getByLeagueIdAndSeasonId(leagueId, season.getId());
            if (!leagueTeamSeasons.isEmpty()) {
                currentSeasons.add(season);
            }
        }
        return currentSeasons;
    }

    public static Set<SeasonPageDto> getCurrentSeasonsForTeam(long teamId, LeagueTeamSeasonService leagueTeamSeasonService, List<SeasonPageDto> allSeasons) {
        Set<SeasonPageDto> currentSeasons = new LinkedHashSet<>();
        for (SeasonPageDto season : allSeasons) {
            List<LeagueTeamSeason> leagueTeamSeasons = leagueTeamSeasonService.getByTeamIdAndSeasonId(teamId, season.getId());
            if (!leagueTeamSeasons.isEmpty()) {
                currentSeasons.add(season);
            }
        }
        return currentSeasons;
    }
}
