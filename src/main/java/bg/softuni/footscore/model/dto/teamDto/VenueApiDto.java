package bg.softuni.footscore.model.dto.teamDto;

public class VenueApiDto {
    private String name;
    private String city;
    private Integer capacity;

    public VenueApiDto() {
    }

    public VenueApiDto(String name, String city, Integer capacity) {
        this.name = name;
        this.city = city;
        this.capacity = capacity;
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
