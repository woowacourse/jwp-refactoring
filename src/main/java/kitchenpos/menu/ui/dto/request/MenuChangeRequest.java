package kitchenpos.menu.ui.dto.request;

import java.math.BigDecimal;

public class MenuChangeRequest {

    private String name;
    private BigDecimal price;

    public MenuChangeRequest() {
    }

    public MenuChangeRequest(String name, BigDecimal price) {
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
