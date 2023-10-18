package kitchenpos.fixture;

import kitchenpos.domain.Menu2;
import kitchenpos.domain.Order;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem2;

public class OrderLineItemFixture {

  public static OrderLineItem2 createOrderLineItem(final Menu2 menu) {
    return new OrderLineItem2(
        menu,
        4L
    );
  }
}
