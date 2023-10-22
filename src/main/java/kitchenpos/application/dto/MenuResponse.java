package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private String name;
    private BigDecimal price;

    private MenuResponse(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getName(), menu.getPrice());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
