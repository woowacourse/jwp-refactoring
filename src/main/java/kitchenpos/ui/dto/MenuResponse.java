package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    private MenuResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice());
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
