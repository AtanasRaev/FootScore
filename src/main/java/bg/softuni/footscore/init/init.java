package bg.softuni.footscore.init;

import bg.softuni.footscore.service.CountryService;
import bg.softuni.footscore.service.LeagueService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class init implements CommandLineRunner {
    private final LeagueService leagueService;
    private final CountryService countryService;


    public init(LeagueService leagueService, CountryService countryService) {
        this.leagueService = leagueService;
        this.countryService = countryService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (this.countryService.isEmpty()) {
            String[] countriesNames = {"england", "germany", "bulgaria", "spain"};
            for (String name : countriesNames) {
                this.countryService.saveCountry(name);
                this.leagueService.saveLeague(name);
            }
        }
    }
}