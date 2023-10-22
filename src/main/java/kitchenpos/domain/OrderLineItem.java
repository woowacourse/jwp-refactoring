package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    private Long menuId;

    @ManyToOne
    private Order order;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long id, Long menuId, Order order, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.order = order;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Order getOrder() {
        return order;
    }

    public long getQuantity() {
        return quantity;
    }
}
