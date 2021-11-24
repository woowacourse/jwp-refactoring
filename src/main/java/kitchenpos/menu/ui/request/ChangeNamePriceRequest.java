package kitchenpos.menu.ui.request;

import java.math.BigDecimal;

public class ChangeNamePriceRequest {
    private String name;
    private BigDecimal price;

    public ChangeNamePriceRequest() {
    }

    public ChangeNamePriceRequest(String name, BigDecimal price) {
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
