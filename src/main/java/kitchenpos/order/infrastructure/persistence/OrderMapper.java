package kitchenpos.order.infrastructure.persistence;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

public class OrderMapper {

  private OrderMapper() {
  }

  public static OrderLineItem mapToOrderLineItem(final OrderLineItemEntity entity) {
    return new OrderLineItem(
        entity.getSeq(),
        entity.getMenuId(),
        entity.getQuantity()
    );
  }

  public static Order mapToOrder(
      final OrderEntity entity,
      final OrderTable orderTable,
      final List<OrderLineItem> orderLineItems
  ) {
    return new Order(
        entity.getId(),
        orderTable,
        OrderStatus.valueOf(entity.getOrderStatus()),
        entity.getOrderedTime(),
        orderLineItems
    );
  }
}
