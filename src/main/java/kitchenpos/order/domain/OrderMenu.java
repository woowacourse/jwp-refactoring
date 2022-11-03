package kitchenpos.order.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;

public class OrderMenu {

    private final Long menuId;
    private final Name name;
    private final Price price;

    public OrderMenu(Long menuId, Name name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu of(Menu menu) {
        return new OrderMenu(menu.getId(), menu.getName(), menu.getPrice());
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
