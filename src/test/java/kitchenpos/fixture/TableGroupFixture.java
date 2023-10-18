package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroup2;

public class TableGroupFixture {

  public static TableGroup2 createTableGroup() {
    return new TableGroup2(LocalDateTime.now());
  }
}
