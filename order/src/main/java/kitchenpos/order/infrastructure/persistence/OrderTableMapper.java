package kitchenpos.order.infrastructure.persistence;

import kitchenpos.order.domain.OrderTable;

public class OrderTableMapper {

  public static OrderTable mapToOrderTable(final OrderTableEntity entity) {
    return new OrderTable(
        entity.getId(),
        entity.getTableGroupId(),
        entity.getNumberOfGuests(),
        entity.isEmpty()
    );
  }
}
