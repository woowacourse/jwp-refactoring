package kitchenpos.dto.request;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.lang.NonNull;

import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidProductPriceException;

public class ProductCreateRequest {
    private final String name;
    private final BigDecimal price;

    @ConstructorProperties({"name", "price"})
    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException(price);
        }

        return Product.of(this.name, this.price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
