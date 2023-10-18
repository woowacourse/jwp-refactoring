package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;

public class OrderFixture {

  public static Order2 createCookingOrder(final OrderTable2 orderTable) {
    return new Order2(
        orderTable,
        OrderStatus.COOKING.name(),
        LocalDateTime.now()
    );
  }

  public static Order2 createCompletionOrder(final OrderTable2 orderTable) {
    return new Order2(
        orderTable,
        OrderStatus.COMPLETION.name(),
        LocalDateTime.now()
    );
  }

  public static Order2 createMealOrder(final OrderTable2 orderTable) {
    return new Order2(
        orderTable,
        OrderStatus.MEAL.name(),
        LocalDateTime.now()
    );
  }
}
