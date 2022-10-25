package kitchenpos.domain.fixture;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    private OrderFixture() {
    }

    public static OrderFixture 주문() {
        return new OrderFixture();
    }

    public OrderFixture 주문_테이블_아이디(final Long orderTableId) {
        this.orderTableId = orderTableId;
        return this;
    }

    public OrderFixture 주문_상태(final String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderFixture 주문한_시간(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public OrderFixture 주문_항목들(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public Order build() {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
