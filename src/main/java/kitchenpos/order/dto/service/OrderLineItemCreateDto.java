package kitchenpos.order.dto.service;

import kitchenpos.menu.domain.Menu;

public class OrderLineItemCreateDto {

    private final Menu menu;

    private final long quantity;

    public OrderLineItemCreateDto(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
