package bg.softuni.footscore.model.dto;

public class ApiCountryDto {
    private String name;
    private String flag;

    public ApiCountryDto() {
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
