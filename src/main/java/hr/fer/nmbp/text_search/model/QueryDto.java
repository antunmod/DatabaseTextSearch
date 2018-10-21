package hr.fer.nmbp.text_search.model;

public class QueryDto {

    public QueryDto(String title, double rank) {
        this.title = title;
        this.rank = rank;
    }

    private String title;
    private double rank;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}
