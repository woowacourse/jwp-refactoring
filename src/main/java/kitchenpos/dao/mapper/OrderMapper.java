package kitchenpos.dao.mapper;

import java.util.List;
import kitchenpos.dao.entity.OrderEntity;
import kitchenpos.dao.entity.OrderLineItemEntity;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderMapper {

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

  public static OrderTable mapToOrderTable(final OrderTableEntity entity) {
    return new OrderTable(
        entity.getId(),
        entity.getTableGroupId(),
        entity.getNumberOfGuests(),
        entity.isEmpty());
  }
}
