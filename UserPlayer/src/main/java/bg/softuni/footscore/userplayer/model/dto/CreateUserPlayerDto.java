package bg.softuni.footscore.userplayer.model.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public class CreateUserPlayerDto {

    private long id;

    @NotEmpty
    private String name;

    @Positive
    @Max(100)
    private int rating;

    @Positive
    private int age;

    @NotEmpty
    private String position;

    private long userId;

    public CreateUserPlayerDto() {
    }

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

