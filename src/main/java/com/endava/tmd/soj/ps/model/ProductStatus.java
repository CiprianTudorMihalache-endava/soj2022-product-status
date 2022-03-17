package com.endava.tmd.soj.ps.model;

public abstract class ProductStatus<T> {
    private double price;
    private boolean available;
    private String availabilityLabel;

    public double getPrice() {
        return price;
    }

    public T setPrice(final double price) {
        this.price = price;
        return self();
    }

    public boolean isAvailable() {
        return available;
    }

    public T setAvailable(final boolean available) {
        this.available = available;
        return self();
    }

    public String getAvailabilityLabel() {
        return availabilityLabel;
    }

    public T setAvailabilityLabel(final String availabilityLabel) {
        this.availabilityLabel = availabilityLabel;
        return self();
    }

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

}
