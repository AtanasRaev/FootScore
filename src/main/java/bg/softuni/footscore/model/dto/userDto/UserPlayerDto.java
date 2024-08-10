package bg.softuni.footscore.model.dto.userDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UserPlayerDto {
    private long id;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String name;

    @Positive
    @Max(100)
    private int rating;

    @Positive
    private int age;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String position;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
