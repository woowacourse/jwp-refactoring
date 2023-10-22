package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private MenuResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static MenuResponse of(Menu menu) {
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
