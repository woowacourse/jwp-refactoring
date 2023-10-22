package kitchenpos.dto.response;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public MenuResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static MenuResponse of(final Menu menu) {
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
