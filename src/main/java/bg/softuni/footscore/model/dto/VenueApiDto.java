package bg.softuni.footscore.model.dto;

public class VenueApiDto {
    private String name;
    private String city;

    public VenueApiDto() {
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
}
