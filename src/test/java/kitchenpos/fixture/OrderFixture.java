package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    private OrderFixture() {
    }

    public static class ORDER {

        public static Order 주문_요청_조리중() {
            return Order.builder()
                    .id(1L)
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COOKING.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(1L, 1L, 1L))
                    .build();
        }

        public static Order 주문_요청_식사중() {
            return Order.builder()
                    .id(2L)
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.MEAL.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(2L, 2L, 2L))
                    .build();
        }

        public static Order 주문_요청_계산_완료() {
            return Order.builder()
                    .id(3L)
                    .orderTableId(1L)
                    .orderStatus(OrderStatus.COMPLETION.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(3L, 3L, 3L))
                    .build();
        }

        public static Order 주문_요청(OrderStatus orderStatus) {
            return Order.builder()
                    .id(3L)
                    .orderTableId(1L)
                    .orderStatus(orderStatus.name())
                    .orderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0))
                    .orderLineItems(getOrderLineItems(3L, 3L, 3L))
                    .build();
        }

        private static List<OrderLineItem> getOrderLineItems(Long... orderId) {
            List<OrderLineItem> orderLineItems = new ArrayList<>();
            for (Long id : orderId) {
                OrderLineItem orderLineItem = OrderLineItem.builder()
                        .seq(1L)
                        .orderId(id)
                        .menuId(1L)
                        .quantity(1L)
                        .build();
                orderLineItems.add(orderLineItem);
            }
            return orderLineItems;
        }
    }
}
