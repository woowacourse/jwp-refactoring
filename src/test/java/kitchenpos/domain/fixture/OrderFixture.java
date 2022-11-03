package kitchenpos.domain.fixture;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    private OrderFixture() {
    }

    public static Orders 주문_1번의_주문_항목들은(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        return 주문()
            .주문_테이블_아이디(orderTableId)
            .주문한_시간(LocalDateTime.now())
            .주문_항목들(orderLineItems)
            .주문_상태(OrderStatus.COOKING)
            .build();
    }

    public static Orders 요리중인_주문(final Long orderTableId) {
        return 주문()
            .주문_테이블_아이디(orderTableId)
            .주문한_시간(LocalDateTime.now())
            .주문_상태(OrderStatus.COOKING)
            .build();
    }

    public static Orders 완료된_주문(final Long orderTableId) {
        return 주문()
            .주문_테이블_아이디(orderTableId)
            .주문한_시간(LocalDateTime.now())
            .주문_상태(OrderStatus.COMPLETION)
            .build();
    }

    private static OrderFixture 주문() {
        return new OrderFixture();
    }

    private OrderFixture 주문_테이블_아이디(final Long orderTableId) {
        this.orderTableId = orderTableId;
        return this;
    }

    private OrderFixture 주문_상태(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    private OrderFixture 주문한_시간(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    private OrderFixture 주문_항목들(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    private Orders build() {
        if (orderLineItems == null) {
            return new Orders(id, orderTableId, orderStatus, orderedTime);
        }
        return new Orders(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
