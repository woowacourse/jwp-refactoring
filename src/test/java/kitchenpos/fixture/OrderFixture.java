package kitchenpos.fixture;

import java.util.Collections;
import java.util.List;
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

  public static Order 빈_주문(final Long orderTableId) {
    final OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1);

    return new Order(null, orderTableId, null, null, Collections.emptyList());
  }

  public static Order 주문_잘못된_메뉴() {
    final OrderLineItem orderLineItem = new OrderLineItem(null, null, 999L, 1);

    return new Order(null, 1L, null, null, List.of(orderLineItem));
  }
}
