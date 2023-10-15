package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private ProductName name;
    private ProductPrice price;

    public Product(final ProductName name, final ProductPrice price) {
        this(null, name, price);
    }

    public Product(final Long id, final ProductName name, final ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
