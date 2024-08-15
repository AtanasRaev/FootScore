package bg.softuni.footscore.userplayer.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users_player")
public class UserPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private int rating;

    @Column
    private int age;

    @Column(nullable = false)
    private String position;

    @Column(name = "user_id")
    private long userId;

    public UserPlayer() {

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
