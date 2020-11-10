package kitchenpos.domain.order;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AttributeOverride(name = "id", column = @Column(name = "order_line_item_id"))
@Entity
public class OrderLineItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ITEM_ORDER"))
    private Order order;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long id, Order order, Long menuId, long quantity) {
        this.id = id;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this(null, order, menuId, quantity);
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
