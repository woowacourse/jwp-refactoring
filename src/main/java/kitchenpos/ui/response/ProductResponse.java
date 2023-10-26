package kitchenpos.ui.response;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private BigDecimal price;
    private String name;

    public ProductResponse() {
    }

    public ProductResponse(final Long id, final BigDecimal price, final String name) {
        this.id = id;
        this.price = price;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
