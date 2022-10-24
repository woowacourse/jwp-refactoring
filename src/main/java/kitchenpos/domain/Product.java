package kitchenpos.domain;

import java.math.BigDecimal;

import org.springframework.data.web.PageableDefault;

public class Product {
    private Long id;

    public Product() {}
    
    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    private String name;
    private BigDecimal price;

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
