package kitchenpos.domain.menu.service.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderMenu;

public class MenuResponse {

    private final Long id;

    private final String name;

    private final Long price;

    public MenuResponse(final Long id, final String name, final Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static MenuResponse toDto(final OrderMenu orderMenu) {
        return new MenuResponse(orderMenu.getId(), orderMenu.getName(),orderMenu.getPrice().getPrice().longValue());
    }

    public static MenuResponse toDto(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(),menu.getPrice().getPrice().longValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
