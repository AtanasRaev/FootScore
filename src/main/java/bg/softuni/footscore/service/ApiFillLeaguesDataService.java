package bg.softuni.footscore.service;

public interface ApiFillLeaguesDataService {
    boolean hasData();
    void saveLeague(String name);
}
