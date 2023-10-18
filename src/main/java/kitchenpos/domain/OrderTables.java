package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {

  private final List<OrderTable2> orderTables;

  public OrderTables(final List<OrderTable2> orderTables) {
    validateOrderTables(orderTables);
    this.orderTables = orderTables;
  }

  private void validateOrderTables(final List<OrderTable2> orderTables) {
    if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
      throw new IllegalArgumentException();
    }
  }

  public List<Long> getOrderTableIds() {
    return orderTables.stream()
        .map(OrderTable2::getId)
        .collect(Collectors.toList());
  }

  public boolean isNotSameSize(final OrderTables orderTables) {
    return this.orderTables.size() != orderTables.orderTables.size();
  }

  public boolean isNotEmptyOrNotBelongTableGroup() {
    return orderTables.stream()
        .anyMatch(orderTable -> !orderTable.isEmpty() || orderTable.isNotBelongTableGroup());
  }

  public List<OrderTable2> ungrouping() {
    return new ArrayList<>(orderTables.stream()
        .map(orderTable -> new OrderTable2(
            orderTable.getId(),
            null,
            orderTable.getNumberOfGuests(),
            false))
        .collect(Collectors.toList()));
  }

  public List<OrderTable2> getOrderTables() {
    return new ArrayList<>(orderTables);
  }
}
