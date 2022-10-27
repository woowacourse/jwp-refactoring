package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductCreateResponse() {
    }

    private ProductCreateResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductCreateResponse from(final Product product) {
        return new ProductCreateResponse(product.getId(), product.getName(), product.getPrice().getValue());
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
