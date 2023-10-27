package kitchenpos.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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

}
