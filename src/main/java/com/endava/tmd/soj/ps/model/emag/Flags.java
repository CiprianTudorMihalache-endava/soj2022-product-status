package com.endava.tmd.soj.ps.model.emag;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Flags {

    @JsonProperty("may_be_ordered")
    private boolean availableForOrder;

    public boolean isAvailableForOrder() {
        return availableForOrder;
    }

}
