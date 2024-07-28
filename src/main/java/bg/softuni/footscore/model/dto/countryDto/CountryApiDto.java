package bg.softuni.footscore.model.dto.countryDto;

public class CountryApiDto {
    private String name;
    private String flag;

    public CountryApiDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
