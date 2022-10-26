package kitchenpos.ui.dto.response;

import java.math.BigDecimal;

public class ProductCreateResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private ProductCreateResponse() {
    }

    public ProductCreateResponse(final Long id, final String name, final BigDecimal price) {
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
