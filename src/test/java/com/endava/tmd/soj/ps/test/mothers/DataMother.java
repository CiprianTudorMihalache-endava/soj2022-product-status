package com.endava.tmd.soj.ps.test.mothers;

import java.util.List;

import com.endava.tmd.soj.ps.model.Data;
import com.endava.tmd.soj.ps.model.ProductHistoricalStatus;
import com.endava.tmd.soj.ps.model.ProductHistory;

public class DataMother {

    private DataMother() {
        // it is useless to instantiate this class
    }

    public static Data phones3Times() {
        final var data = new Data();

        data.setHeaders(List.of("2022-03-25 12:13:14", "2022-03-20 13:14:15", "2022-03-17 14:15:16"));

        final var p1History = new ProductHistory();
        p1History.setTitle("Telefon mobil Samsung Galaxy S22, Dual SIM, 128GB, 8GB RAM, 5G, Phantom Black");
        p1History.setStatusHistory(List.of(
                new ProductHistoricalStatus()
                        .setPrice(4199.98)
                        .setAvailable(true)
                        .setAvailabilityLabel("\u00cen stoc"),
                new ProductHistoricalStatus()
                        .setPrice(4099.98)
                        .setAvailable(true)
                        .setAvailabilityLabel("Precomanda")));

        final var p2History = new ProductHistory();
        p2History.setTitle("Telefon mobil Samsung Galaxy S21, Dual SIM, 128GB, 8GB RAM, 5G, Phantom Grey ");
        p2History.setStatusHistory(List.of(
                new ProductHistoricalStatus()
                        .setPrice(3249.99)
                        .setAvailable(true)
                        .setAvailabilityLabel("ultimele 2 produse"),
                new ProductHistoricalStatus()
                        .setPrice(3299.99)
                        .setAvailable(true)
                        .setAvailabilityLabel("\u00cen stoc"),
                new ProductHistoricalStatus()
                        .setPrice(3499.98)
                        .setAvailable(false)
                        .setAvailabilityLabel("Stoc epuizat")));

        final var p3History = new ProductHistory();
        p3History.setTitle("Telefon mobil Samsung Galaxy S21, Dual SIM, 128GB, 8GB RAM, 5G, Phantom Grey ");
        p3History.setStatusHistory(List.of(
                new ProductHistoricalStatus()
                        .setPrice(3399)
                        .setAvailable(true)
                        .setAvailabilityLabel("\u00cen stoc"),
                new ProductHistoricalStatus()
                        .setPrice(3299.99)
                        .setAvailable(false)
                        .setAvailabilityLabel("Stoc epuizat"),
                new ProductHistoricalStatus()
                        .setPrice(3499.98)
                        .setAvailable(true)
                        .setAvailabilityLabel("Ultimul produs in stoc")));

        final var products = data.getProducts();
        products.put(
                "https://www.emag.ro/telefon-mobil-samsung-galaxy-s22-dual-sim-128gb-8gb-ram-5g-phantom-black-sm-s901bzkdeue/pd/DTZ01FMBM/",
                p1History);
        products.put(
                "https://www.emag.ro/telefon-mobil-samsung-galaxy-s21-dual-sim-128gb-8gb-ram-5g-phantom-grey-sm-g991bzadeue/pd/DPLGTDMBM/",
                p2History);
        products.put(
                "https://www.emag.ro/telefon-mobil-samsung-galaxy-s20-dual-sim-128gb-8gb-ram-4g-cosmic-gray-sm-g980fzadeue/pd/D4KBRWBBM/",
                p3History);

        return data;
    }
}
