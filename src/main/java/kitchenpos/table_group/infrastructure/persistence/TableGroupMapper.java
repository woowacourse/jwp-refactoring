package kitchenpos.table_group.infrastructure.persistence;

import java.util.List;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.table_group.domain.TableGroup;

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
