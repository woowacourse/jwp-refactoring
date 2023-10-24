package kitchenpos.order_table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {

  private final List<OrderTable> orderTables;

  public OrderTables(final List<OrderTable> orderTables) {
    validateOrderTables(orderTables);
    this.orderTables = orderTables;
  }

  private void validateOrderTables(final List<OrderTable> orderTables) {
    if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
      throw new IllegalArgumentException();
    }
  }

  public List<Long> getOrderTableIds() {
    return orderTables.stream()
        .map(OrderTable::getId)
        .collect(Collectors.toList());
  }

  public void validateMatchingOrderTableSize(final int orderTableSize) {
    if (orderTables.size() != orderTableSize) {
      throw new IllegalArgumentException();
    }
  }

  public void validateEmptyOrBelongTableGroup() {
    if (orderTables.stream()
        .anyMatch(orderTable -> !orderTable.isEmpty() || orderTable.isBelongTableGroup())) {
      throw new IllegalArgumentException();
    }
  }

  public List<OrderTable> ungrouping() {
    return orderTables.stream()
        .map(orderTable -> new OrderTable(
            orderTable.getId(),
            null,
            orderTable.getNumberOfGuests(),
            false))
        .collect(Collectors.toList());
  }

  public List<OrderTable> getOrderTables() {
    return new ArrayList<>(orderTables);
  }
}
