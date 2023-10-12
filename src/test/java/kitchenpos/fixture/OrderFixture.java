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
            final Order order = new Order();
            order.setId(1L);
            order.setOrderTableId(1L);
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0));
            order.setOrderLineItems(getOrderLineItems(order.getId(), order.getId(), order.getId()));
            return order;
        }

        public static Order 주문_요청_식사중() {
            final Order order = new Order();
            order.setId(2L);
            order.setOrderTableId(2L);
            order.setOrderStatus(OrderStatus.MEAL.name());
            order.setOrderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0));

            order.setOrderLineItems(getOrderLineItems(order.getId(), order.getId(), order.getId(), order.getId()));
            return order;
        }

        public static Order 주문_요청_계산_완료() {
            final Order order = new Order();
            order.setId(3L);
            order.setOrderTableId(3L);
            order.setOrderStatus(OrderStatus.COMPLETION.name());
            order.setOrderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0));

            order.setOrderLineItems(getOrderLineItems(order.getId()));
            return order;
        }

        public static Order 주문_요청(OrderStatus orderStatus) {
            final Order order = new Order();
            order.setId(1L);
            order.setOrderTableId(1L);
            order.setOrderStatus(orderStatus.name());
            order.setOrderedTime(LocalDateTime.of(2021, 8, 24, 0, 0, 0, 0));
            order.setOrderLineItems(getOrderLineItems(order.getId(), order.getId(), order.getId()));
            return order;
        }

        private static List<OrderLineItem> getOrderLineItems(Long... orderIds) {
            List<OrderLineItem> orderLineItems = new ArrayList<>();
            for (Long id : orderIds) {
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setSeq(1L);
                orderLineItem.setOrderId(id);
                orderLineItem.setMenuId(1L);
                orderLineItem.setQuantity(1);
                orderLineItems.add(orderLineItem);
            }
            return orderLineItems;
        }
    }
}
