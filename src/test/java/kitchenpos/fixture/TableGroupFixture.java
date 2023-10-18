package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroup2;

public class TableGroupFixture {

  public static TableGroup2 createTableGroup() {
    return new TableGroup2(LocalDateTime.now(), null);
  }

  public static TableGroup2 createTableGroup(final List<OrderTable2> orderTables) {
    return new TableGroup2(LocalDateTime.now(), orderTables);
  }
}
