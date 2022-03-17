package com.endava.tmd.soj.ps.model;

public class ProductCurrentStatus extends ProductStatus<ProductCurrentStatus> {
    private String title;

    public String getTitle() {
        return title;
    }

    public ProductCurrentStatus setTitle(final String title) {
        this.title = title;
        return this;
    }

}
