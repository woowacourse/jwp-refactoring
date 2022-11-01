package kitchenpos.domain.order;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class OrderLineItem {
    @Embedded
    private OrderMenuItem orderMenuItem;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final String menuName, final BigDecimal menuPrice, final long quantity) {
        this.orderMenuItem = new OrderMenuItem(menuName, menuPrice);
        this.quantity = quantity;
    }

    public OrderMenuItem getOrderMenuItem() {
        return orderMenuItem;
    }

    public String getMenuName() {
        return orderMenuItem.getMenuName();
    }

    public BigDecimal getMenuPrice() {
        return orderMenuItem.getMenuPrice();
    }

    public long getQuantity() {
        return quantity;
    }
}
