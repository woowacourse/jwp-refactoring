package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    private Long menuId;

    @Column(name = "order_id")
    private Long orderId;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long id, Long menuId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long id, Long menuId, Long orderId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }
}
