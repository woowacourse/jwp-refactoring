package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.table_group.domain.TableGroup;

public class TableGroupFixture {

  public static TableGroup createTableGroup() {
    return new TableGroup(LocalDateTime.now(), null);
  }

  public static TableGroup createTableGroup(final List<OrderTable> orderTables) {
    return new TableGroup(LocalDateTime.now(), orderTables);
  }
}
