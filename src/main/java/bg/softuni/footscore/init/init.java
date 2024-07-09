package bg.softuni.footscore.init;

import bg.softuni.footscore.service.impl.ApiFillCountryDataImpl;
import bg.softuni.footscore.service.impl.ApiFillLeaguesDataServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class init implements CommandLineRunner {
    private final ApiFillLeaguesDataServiceImpl apiFillLeaguesDataService;
    private final ApiFillCountryDataImpl apiFillCountryDataService;


    public init(ApiFillLeaguesDataServiceImpl apiFillDataService, ApiFillCountryDataImpl apiFillCountryDataService) {
        this.apiFillLeaguesDataService = apiFillDataService;
        this.apiFillCountryDataService = apiFillCountryDataService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (this.apiFillCountryDataService.isEmpty()) {
            String[] countriesNames = {"england", "germany", "bulgaria", "spain"};
            for (String name : countriesNames) {
                this.apiFillCountryDataService.saveCountry(name);
                this.apiFillLeaguesDataService.saveLeague(name);

            }
        }
    }
}