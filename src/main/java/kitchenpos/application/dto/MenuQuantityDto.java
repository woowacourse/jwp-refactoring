package kitchenpos.application.dto;

import kitchenpos.domain.Menu;

public class MenuQuantityDto {

    private Menu menu;
    private long quantity;

    public MenuQuantityDto(Menu menu, long quantity) {
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
