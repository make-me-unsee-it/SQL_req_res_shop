package com.step.hryshkin.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Good {
    private Long id;
    private String title;
    private BigDecimal price;
    private String country;

    public Good(Long id, String title, BigDecimal price, String country) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.country = country;
    }

    public Good(String title, BigDecimal price, String country) {
        this.title = title;
        this.price = price;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Good)) return false;
        Good good = (Good) o;
        return id.equals(good.id) && title.equals(good.title) && price.equals(good.price) && country.equals(good.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, country);
    }

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", country='" + country + '\'' +
                '}';
    }
}

