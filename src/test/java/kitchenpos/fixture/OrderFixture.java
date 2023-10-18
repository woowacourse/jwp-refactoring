package kitchenpos.fixture;

import java.util.List;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderFixture {

  public static Order 주문(final Long orderTableId) {
    final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);

    return new Order(null, orderTableId, null, null, List.of(orderLineItem));
  }

  public static Order 주문(final Long orderTableId, final String status) {
    final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);

    return new Order(null, orderTableId, status, null, List.of(orderLineItem));
  }

  public static OrderCreateRequest getOrderRequest(final Long orderTableId) {
    final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L,
        1);
    return new OrderCreateRequest(orderTableId, List.of(orderLineItemCreateRequest));
  }
}
