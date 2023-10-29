package kitchenpos.product.application.request;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductRequest {
    private final String name;
    private final BigDecimal price;

    public ProductRequest(final String name,
                          final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductRequest that = (ProductRequest) o;
        return Objects.equals(name, that.name)
                && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
