package bg.softuni.footscore.model.dto.teamDto;

public class VenuePageDto {
    private long id;
    private String name;
    private String city;
    private int capacity;

    public VenuePageDto(String name, String city, Integer capacity) {
            this.name = name;
            this.city = city;
            this.capacity = capacity;
    }

    public VenuePageDto() {
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
