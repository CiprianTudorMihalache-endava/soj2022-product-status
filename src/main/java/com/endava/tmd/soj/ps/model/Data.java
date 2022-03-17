package com.endava.tmd.soj.ps.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    private List<String> headers = new ArrayList<>();
    private Map<String, ProductHistory> products = new HashMap<>();

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(final List<String> headers) {
        this.headers = headers;
    }

    public Map<String, ProductHistory> getProducts() {
        return products;
    }

    public void setProducts(final Map<String, ProductHistory> products) {
        this.products = products;
    }

}
