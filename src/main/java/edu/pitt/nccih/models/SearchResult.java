package edu.pitt.nccih.models;

import java.util.List;

public class SearchResult {

    private Integer total;
    private List<Annotation> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Annotation> getRows() {
        return rows;
    }

    public void setRows(List<Annotation> rows) {
        this.rows = rows;
    }
}