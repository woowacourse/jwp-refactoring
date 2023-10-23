package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

  public static OrderLineItem createOrderLineItem(final Menu menu) {
    return new OrderLineItem(
        menu.getId(),
        4L
    );
  }
}
