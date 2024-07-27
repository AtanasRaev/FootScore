package bg.softuni.footscore.init;

import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import bg.softuni.footscore.service.SeasonService;
import bg.softuni.footscore.service.TeamService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class init implements CommandLineRunner {
    private final LeagueService leagueService;
    private final CountryService countryService;
    private final SeasonService seasonService;


    public init(LeagueService leagueService,
                CountryService countryService,
                SeasonService seasonService) {
        this.leagueService = leagueService;
        this.countryService = countryService;
        this.seasonService = seasonService;
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