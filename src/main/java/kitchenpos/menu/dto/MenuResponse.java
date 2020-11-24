package kitchenpos.menu.dto;

import kitchenpos.generic.Price;

public class MenuResponse {
    private final Long menuId;
    private final Price price;

    public MenuResponse(Long menuId, Price price) {
        this.menuId = menuId;
        this.price = price;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Price getPrice() {
        return price;
    }
}
