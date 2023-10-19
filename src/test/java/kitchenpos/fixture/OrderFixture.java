package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;

public class OrderFixture {

  public static Order2 createCookingOrder(final OrderTable2 orderTable) {
    return new Order2(
        orderTable,
        OrderStatus.COOKING,
        LocalDateTime.now(),
        null
    );
  }

  public static Order2 createCompletionOrder(final OrderTable2 orderTable) {
    return new Order2(
        orderTable,
        OrderStatus.COMPLETION,
        LocalDateTime.now(),
        null
    );
  }

  public static Order2 createCompletionOrderWithOrderLineItems(
      final OrderTable2 orderTable,
      final List<OrderLineItem2> orderLineItems
  ) {
    return new Order2(
        orderTable,
        OrderStatus.COMPLETION,
        LocalDateTime.now(),
        orderLineItems
    );
  }

  public static Order2 createMealOrder(final OrderTable2 orderTable) {
    return new Order2(
        orderTable,
        OrderStatus.MEAL,
        LocalDateTime.now(),
        null
    );
  }

  public static Order2 createMealOrderWithOrderLineItems(
      final OrderTable2 orderTable,
      final List<OrderLineItem2> orderLineItems
  ) {
    return new Order2(
        null,
        orderTable,
        OrderStatus.MEAL,
        LocalDateTime.now(),
        orderLineItems
    );
  }
}
