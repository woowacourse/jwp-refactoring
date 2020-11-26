package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import kitchenpos.domain.product.Product;

public class ProductRequest {

    @NotEmpty
    private String name;

    @NotNull
    private BigDecimal price;

    private ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return Product.entityOf(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductRequest that = (ProductRequest) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
            "name='" + name + '\'' +
            ", price=" + price +
            '}';
    }
}
