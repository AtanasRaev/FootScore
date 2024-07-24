package bg.softuni.footscore.init;

import bg.softuni.footscore.model.entity.League;
import bg.softuni.footscore.model.entity.Season;
import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.TeamService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class init implements CommandLineRunner {
    private final LeagueService leagueService;
    private final CountryService countryService;
    private final SeasonService seasonService;
    private final TeamService teamService;


    public init(LeagueService leagueService,
                CountryService countryService,
                SeasonService seasonService,
                TeamService teamService) {
        this.leagueService = leagueService;
        this.countryService = countryService;
        this.seasonService = seasonService;
        this.teamService = teamService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (this.seasonService.isEmpty() && this.countryService.isEmpty()) {
            String[] countriesNames = {"england", "germany", "bulgaria", "spain"};

            this.seasonService.saveApiSeasons(countriesNames[0]);

            for (String name : countriesNames) {
                this.countryService.saveCountry(name);
                this.leagueService.saveApiLeagues(name);
            }
        }
    }
}