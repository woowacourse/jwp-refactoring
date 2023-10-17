package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.util.Collections;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_망고치킨_2개() {
        final var order = new Order();
        order.setOrderTableId(OrderTableFixture.주문테이블_2명_id_1().getId());
        order.setOrderLineItems(Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개()));
        return order;
    }

    public static Order 주문_망고치킨_2개_식사() {
        final var order = new Order();
        order.setOrderTableId(OrderTableFixture.주문테이블_2명_id_1().getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderLineItems(Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개()));
        return order;
    }

    public static Order 주문_망고치킨_2개_주문완료() {
        final var order = new Order();
        order.setOrderTableId(OrderTableFixture.주문테이블_2명_id_1().getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderLineItems(Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개()));
        return order;
    }
}
