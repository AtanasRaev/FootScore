package bg.softuni.footscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FootScoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(FootScoreApplication.class, args);
    }

}
