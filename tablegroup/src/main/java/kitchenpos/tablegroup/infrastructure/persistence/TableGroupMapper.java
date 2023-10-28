package kitchenpos.tablegroup.infrastructure.persistence;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupMapper {

  private TableGroupMapper() {
  }

  public static TableGroup mapToTableGroup(
      final TableGroupEntity entity,
      final List<OrderTable> orderTables
  ) {
    return new TableGroup(entity.getId(), entity.getCreatedDate(), orderTables);
  }
}