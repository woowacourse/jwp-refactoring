package kitchenpos.product.application.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse(final Product product) {
        this(product.getId(), product.getName(), product.getPrice());
    }

    @JsonCreator
    public ProductResponse(final Long id, final String name, final BigDecimal price) {
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
