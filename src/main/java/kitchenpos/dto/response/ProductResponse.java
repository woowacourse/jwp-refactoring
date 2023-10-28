package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice());
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
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
