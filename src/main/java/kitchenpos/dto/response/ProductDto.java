package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public ProductDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice());
    }

    public ProductDto(String name, BigDecimal price) {
        this(null, name, price);
    }

    public ProductDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
