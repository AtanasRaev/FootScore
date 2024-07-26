package bg.softuni.footscore.utils;

import bg.softuni.footscore.model.entity.LeagueTeamSeason;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.service.LeagueTeamSeasonService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SeasonUtils {

    public static Set<Season> getCurrentSeasonsForLeague(long leagueId, LeagueTeamSeasonService leagueTeamSeasonService, List<Season> allSeasons) {
        Set<Season> currentSeasons = new LinkedHashSet<>();
        for (Season season : allSeasons) {
            List<LeagueTeamSeason> leagueTeamSeasons = leagueTeamSeasonService.getByLeagueIdAndSeasonId(leagueId, season.getId());
            if (!leagueTeamSeasons.isEmpty()) {
                currentSeasons.add(season);
            }
        }
        return currentSeasons;
    }

    public static Set<Season> getCurrentSeasonsForTeam(long teamId, LeagueTeamSeasonService leagueTeamSeasonService, List<Season> allSeasons) {
        Set<Season> currentSeasons = new LinkedHashSet<>();
        for (Season season : allSeasons) {
            List<LeagueTeamSeason> leagueTeamSeasons = leagueTeamSeasonService.getByTeamIdAndSeasonId(teamId, season.getId());
            if (!leagueTeamSeasons.isEmpty()) {
                currentSeasons.add(season);
            }
        }
        return currentSeasons;
    }
}
