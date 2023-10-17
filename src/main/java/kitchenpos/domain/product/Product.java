package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private Long id;
    private ProductName name;
    private ProductPrice price;

    public Product(ProductName name, ProductPrice price) {
        this(null, name, price);
    }

    public Product(Long id, ProductName name, ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
