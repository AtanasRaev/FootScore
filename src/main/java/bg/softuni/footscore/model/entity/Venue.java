package bg.softuni.footscore.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String city;

    @Column
    private Integer capacity;

    public Venue() {
    }
    public Venue(String name, String city, Integer capacity) {
        this.name = name;
        this.city = city;
        this.capacity = capacity;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
