package hr.fer.nmbp.text_search.model;

import lombok.Data;

import java.util.List;

@Data
public class Analysis {

    private List<String> columns;
    private List<Object[]> rows;

    public Analysis(List<String> columns, List<Object[]> rows) {
        this.columns = columns;
        this.rows = rows;
    }
}
