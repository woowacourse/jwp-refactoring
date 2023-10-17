package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.dto.request.order.OrderLineItemsDto;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.order = Order.builder().id(orderId).build();
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem from(final OrderLineItemsDto dto) {
        return new OrderLineItem(
                dto.getSeq(),
                dto.getOrderId(),
                dto.getMenuId(),
                dto.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
