package kitchenpos.order_table.infrastructure.persistence;

import kitchenpos.order_table.domain.OrderTable;

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
