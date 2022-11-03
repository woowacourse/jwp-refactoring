package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public ProductResponse(final Product product) {
        this(product.getId(), product.getName(), product.getPrice().getValue());
    }

    public ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(id, name, price);
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
