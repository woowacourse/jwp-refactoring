package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

  public static OrderLineItem createOrderLineItem(final Menu menu) {
    return new OrderLineItem(
        menu.getId(),
        4L
    );
  }
}
