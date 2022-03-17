package com.endava.tmd.soj.ps.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserPreferences {
    private String reportFileName;
    private boolean deleteOldReport;
    private List<String> products = new ArrayList<>();

    public String getReportFileName() {
        return reportFileName;
    }

    public boolean isDeleteOldReport() {
        return deleteOldReport;
    }

    public List<String> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
