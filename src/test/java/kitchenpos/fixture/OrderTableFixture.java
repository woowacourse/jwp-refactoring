package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

  public static OrderTable createEmptySingleOrderTable() {
    return new OrderTable(
        null,
        5,
        true
    );
  }

  public static OrderTable createNotEmptySingleOrderTable() {
    return new OrderTable(
        null,
        5,
        false
    );
  }
}
