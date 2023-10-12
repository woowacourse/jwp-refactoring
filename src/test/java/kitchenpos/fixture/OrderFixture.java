package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_엔티티_A = createOrder(1L, OrderStatus.MEAL, List.of(OrderLineItemFixture.주문_아이템_엔티티_A));
    public static Order 주문_엔티티_B_완료 = createOrder(1L, OrderStatus.COMPLETION, List.of(OrderLineItemFixture.주문_아이템_엔티티_A));
    public static Order 주문_엔티티_B_주문_아이템_없음 = createOrder(2L, OrderStatus.MEAL, Collections.emptyList());

    private static Order createOrder(Long id, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        OrderTable 주문_테이블_A = OrderTableFixture.주문_테이블_A;

        Order order = new Order();
        order.setId(id);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(주문_테이블_A.getId());
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
