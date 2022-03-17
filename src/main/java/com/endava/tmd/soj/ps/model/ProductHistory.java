package com.endava.tmd.soj.ps.model;

import java.util.ArrayList;
import java.util.List;

public class ProductHistory {
    private String title;
    private List<ProductHistoricalStatus> statusHistory = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<ProductHistoricalStatus> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(final List<ProductHistoricalStatus> statusHistory) {
        this.statusHistory = statusHistory;
    }

}
