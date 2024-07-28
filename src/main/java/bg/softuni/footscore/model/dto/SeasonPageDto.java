package bg.softuni.footscore.model.dto;

public class SeasonPageDto {
    private long id;
    private int year;

    public SeasonPageDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return this.year + "/" + (this.year + 1);
    }
}
