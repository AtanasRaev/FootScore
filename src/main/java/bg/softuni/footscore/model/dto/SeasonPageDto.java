package bg.softuni.footscore.model.dto;

public class SeasonPageDto {
    private long id;
    private Integer year;

    public SeasonPageDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return this.year + "/" + (this.year + 1);
    }
}
