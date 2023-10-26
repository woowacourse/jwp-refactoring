package kitchenpos.Product.domain;

import kitchenpos.Product.domain.vo.ProductName;
import kitchenpos.price.domain.vo.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ProductName productName;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final String productName,
                   final BigDecimal price) {
        this(null, productName, price);
    }

    public Product(final Long id,
                   final String productName,
                   final BigDecimal price) {
        this.id = id;
        this.productName = ProductName.from(productName);
        this.price = Price.from(price);
    }

    public Long getId() {
        return id;
    }

    public ProductName getProductName() {
        return productName;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
