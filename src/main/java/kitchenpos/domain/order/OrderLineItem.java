package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column
    private Long orderId;

    @Column
    private Long menuId;

    @Column
    private Integer quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, Integer quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, Long menuId, Integer quantity) {
        this(null, orderId, menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
