package kitchenpos.fixture;

import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;

public class OrderTableFixture {

  public static OrderTable2 createNotEmptyOrderTable(final TableGroup2 tableGroup) {
    return new OrderTable2(
        tableGroup,
        5,
        false
    );
  }

  public static OrderTable2 createEmptyOrderTable(final TableGroup2 tableGroup) {
    return new OrderTable2(
        tableGroup,
        5,
        true
    );
  }
}
