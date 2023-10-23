package kitchenpos.dao.mapper;

import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.OrderTable;

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
