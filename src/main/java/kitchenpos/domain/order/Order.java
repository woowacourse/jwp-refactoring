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
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseCreateTimeEntity {

    private static final OrderStatus INITIAL_ORDER_STATUS = OrderStatus.COOKING;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    Order(final Long orderTableId, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static Order createNewOrder(final Long orderTableId, final OrderLineItems orderLineItems, OrderValidator orderValidator) {
        final Order order = new Order(orderTableId, INITIAL_ORDER_STATUS, orderLineItems);
        orderValidator.validate(order);

        return order;
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

    public Set<Long> getOrderLineItemMenuIds() {
        return orderLineItems.getMenuIds();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
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
