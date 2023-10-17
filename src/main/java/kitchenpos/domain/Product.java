package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.isNull;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(final String name, final BigDecimal price) {
        if (isNull(name)) {
            throw new IllegalArgumentException("상품명이 없습니다.");
        }
        if (isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품 가격이 없거나 0보다 작습니다.");
        }
        this.name = name;
        this.price = price;
    }


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}
