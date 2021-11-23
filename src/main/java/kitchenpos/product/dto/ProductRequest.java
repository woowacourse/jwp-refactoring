package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    protected ProductRequest() {
    }

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductRequest request = (ProductRequest) o;
        return Objects.equals(name, request.name) && Objects.equals(price,
            request.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
