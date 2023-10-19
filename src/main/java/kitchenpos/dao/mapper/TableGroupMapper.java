package kitchenpos.dao.mapper;

import java.util.List;
import kitchenpos.dao.entity.TableGroupEntity;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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
