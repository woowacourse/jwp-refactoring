package kitchenpos.dto.product.response;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private ProductResponse() {
    }

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
