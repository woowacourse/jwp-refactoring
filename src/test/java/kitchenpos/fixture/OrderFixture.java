package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

  public static Order 주문() {
    final OrderLineItem orderLineItem = new OrderLineItem();
    orderLineItem.setMenuId(1L);
    orderLineItem.setQuantity(1);

    final Order order = new Order();
    order.setOrderTableId(1L);
    order.setOrderLineItems(List.of(orderLineItem));
    return order;
  }

  public static Order 주문_잘못된_메뉴() {
    final OrderLineItem orderLineItem = new OrderLineItem();
    orderLineItem.setMenuId(999L);
    orderLineItem.setQuantity(1);

    final Order order = new Order();
    order.setOrderTableId(1L);
    order.setOrderLineItems(List.of(orderLineItem));
    return order;
  }
}
