package com.endava.tmd.soj.ps.service;

import com.endava.tmd.soj.ps.model.ProductCurrentStatus;

public interface ContentReader {
    ProductCurrentStatus readCurrentStatus(final String responseContent);
}
