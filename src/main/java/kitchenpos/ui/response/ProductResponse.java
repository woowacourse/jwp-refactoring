package kitchenpos.ui.response;

import java.math.BigDecimal;

public class ProductResponse {

    private final long id;
    private final String name;
    private final BigDecimal price;

    public ProductResponse(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
