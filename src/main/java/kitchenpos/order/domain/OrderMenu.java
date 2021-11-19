package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.menu.domain.Menu;
import kitchenpos.name.Name;
import kitchenpos.price.Price;

@Embeddable
public class OrderMenu {

    private Long menuId;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    protected OrderMenu() {
    }

    public OrderMenu(final Menu menu) {
        this(menu.getId(), menu.getName(), menu.getPrice());
    }

    public OrderMenu(final Long menuId, final Name name, final Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
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
