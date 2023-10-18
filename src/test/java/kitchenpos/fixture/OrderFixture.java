package kitchenpos.fixture;

import java.util.List;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;

public class OrderFixture {

  public static OrderCreateRequest getOrderRequest(final Long orderTableId) {
    final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L,
        1);
    return new OrderCreateRequest(orderTableId, List.of(orderLineItemCreateRequest));
  }
}
