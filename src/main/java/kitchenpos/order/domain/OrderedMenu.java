package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class OrderedMenu {

    @Column
    private String menuName;

    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "menu_price"))
    private Price menuPrice;

    public OrderedMenu() {
    }

    public OrderedMenu(final String menuName, final Price menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getPrice();
    }
}
