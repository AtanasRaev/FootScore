package bg.softuni.footscore.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String position;

    @Column
    private Integer age;

    @Column
    private LocalDate birthday;

    @Column
    private String nationality;

    @Column
    private Integer height;

    @Column
    private Integer weight;

    @Column
    private String photo;

    @Column
    private long apiId;

    @Column
    private String team;

    @Column
    private Integer number;

    @Column
    private Boolean isRetired;

    @Column
    private Boolean isSelected;

    public Player(String firstName,
                  String lastName,
                  String name,
                  Integer age,
                  LocalDate birthday,
                  String nationality,
                  Integer height,
                  Integer weight,
                  String photo,
                  long apiId) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.shortName = name;
        this.setFullName();
        this.age = age;
        this.birthday = birthday;
        this.nationality = nationality;
        this.height = height;
        this.weight = weight;
        this.photo = photo;
        this.apiId = apiId;
        this.isSelected = false;
    }

    public Player() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    private void setFullName() {
        this.fullName = this.getFirstName() + " " + this.getLastName();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getApiId() {
        return apiId;
    }

    public void setApiId(long apiId) {
        this.apiId = apiId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean isRetired() {
        return isRetired;
    }

    public void setRetired(Boolean retired) {
        isRetired = retired;
    }

}
