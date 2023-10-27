package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.generic.domain.Price;

@Embeddable
public class OrderedMenu {

    private String menuName;
    @Embedded
    private Price menuPrice;

    protected OrderedMenu() {
    }

    public OrderedMenu(final String menuName, final Price menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }
}
