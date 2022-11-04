package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItem {

    private Long menuId;

    private String menuName;

    private BigDecimal price;

    @Column(nullable = false)
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final String menuName, final BigDecimal price, final long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getMenuName() {
        return menuName;
    }

    public long getQuantity() {
        return quantity;
    }
}
