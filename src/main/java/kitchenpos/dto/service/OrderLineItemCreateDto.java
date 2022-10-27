package kitchenpos.dto.service;

import kitchenpos.domain.Menu;

public class OrderLineItemCreateDto {

    private Menu menu;

    private long quantity;

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
