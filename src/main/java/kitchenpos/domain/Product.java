package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 메뉴를 관리하는 기준이 되는 객체
 */

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    public Product(final String name, final BigDecimal price) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Product product = (Product)o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
