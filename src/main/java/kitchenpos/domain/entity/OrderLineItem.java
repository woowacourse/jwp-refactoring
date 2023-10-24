package kitchenpos.domain.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.value.Quantity;
import kitchenpos.dto.OrderLineItemsDto;

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

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, Quantity quantity) {
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
                new Quantity(dto.getQuantity())
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

    public Quantity getQuantity() {
        return quantity;
    }
}
