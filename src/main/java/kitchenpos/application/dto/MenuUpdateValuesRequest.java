package kitchenpos.application.dto;

import java.math.BigDecimal;

public class MenuUpdateValuesRequest {

    private String name;
    private BigDecimal price;

    protected MenuUpdateValuesRequest() {
    }

    public MenuUpdateValuesRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
