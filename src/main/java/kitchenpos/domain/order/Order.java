package kitchenpos.domain.order;

import kitchenpos.common.BaseCreateTimeEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }
    public Order(final Long orderTableId, final OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public static Order createNewOrder(final Long orderTableId, final OrderValidator orderValidator) {
        orderValidator.validate(orderTableId);
        return new Order(orderTableId, OrderStatus.COOKING);
    }

    public void addOrderLineItems(final OrderLineItems orderLineItems) {
        for (final OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
            this.orderLineItems.add(orderLineItem);
            orderLineItem.initOrder(this);
        }
    }

    public boolean isNotComplete() {
        return orderStatus != OrderStatus.COMPLETION;
    }

    public void changeOrderStatus(final OrderStatus newOrderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("완료된 주문의 상태를 변경할 수 없습니다.");
        }

        this.orderStatus = newOrderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
