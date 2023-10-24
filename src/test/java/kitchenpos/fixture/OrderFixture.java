package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order_table.domain.OrderTable;

public class OrderFixture {

  public static Order createCookingOrder(final OrderTable orderTable) {
    return new Order(
        orderTable,
        OrderStatus.COOKING,
        LocalDateTime.now(),
        null
    );
  }

  public static Order createCompletionOrder(final OrderTable orderTable) {
    return new Order(
        orderTable,
        OrderStatus.COMPLETION,
        LocalDateTime.now(),
        null
    );
  }

  public static Order createCompletionOrderWithOrderLineItems(
      final OrderTable orderTable,
      final List<OrderLineItem> orderLineItems
  ) {
    return new Order(
        orderTable,
        OrderStatus.COMPLETION,
        LocalDateTime.now(),
        orderLineItems
    );
  }

  public static Order createMealOrder(final OrderTable orderTable) {
    return new Order(
        orderTable,
        OrderStatus.MEAL,
        LocalDateTime.now(),
        null
    );
  }

  public static Order createMealOrderWithOrderLineItems(
      final OrderTable orderTable,
      final List<OrderLineItem> orderLineItems
  ) {
    return new Order(
        null,
        orderTable,
        OrderStatus.MEAL,
        LocalDateTime.now(),
        orderLineItems
    );
  }
}
