package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order 주문_상품_없이_생성(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order 주문_상태_변경(Order order, String orderStatus) {
        order.setOrderStatus(orderStatus);
        return order;
    }
}
