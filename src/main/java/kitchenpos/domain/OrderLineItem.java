package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
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

    public OrderLineItem assignOrder(Order order) {
        if (this.order != null) {
            throw new IllegalArgumentException("이미 해당 주문 상품은 주문에 포함되었습니다.");
        }
        this.order = order;
        return this;
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
