package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import suppoert.domain.BaseEntity;

@Entity
public class OrderLineItem extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders"))
    private Order order;
    private Long menuId;
    @Column(nullable = false)
    private long quantity;

    public OrderLineItem(final Order order, final Long menuId, final long quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
        order.getOrderLineItems().add(this);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    protected OrderLineItem() {
    }
}
