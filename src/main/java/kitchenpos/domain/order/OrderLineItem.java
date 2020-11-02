package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@AttributeOverride(name = "id", column = @Column(name = "ORDER_LINE_ITEM_ID"))
@Entity
public class OrderLineItem extends BaseEntity {
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId) {
        this.menuId = menuId;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Order getOrder() {
        return order;
    }
}
