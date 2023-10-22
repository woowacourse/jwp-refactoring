package kitchenpos.order_table.application.entity;

import kitchenpos.order_table.domain.OrderTable;

public class OrderTableEntity {

  private Long id;
  private Long tableGroupId;
  private int numberOfGuests;
  private boolean empty;

  public OrderTableEntity(final Long id, final Long tableGroupId, final int numberOfGuests,
      final boolean empty) {
    this.id = id;
    this.tableGroupId = tableGroupId;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public OrderTableEntity() {
  }

  public Long getId() {
    return id;
  }

  public Long getTableGroupId() {
    return tableGroupId;
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public boolean isEmpty() {
    return empty;
  }

  public static OrderTableEntity from(final OrderTable orderTable) {
    return new OrderTableEntity(orderTable.getId(), orderTable.getTableGroupId(),
        orderTable.getNumberOfGuests(), orderTable.isEmpty());
  }

  public OrderTable toOrderTable() {
    return new OrderTable(id, tableGroupId, numberOfGuests, empty);
  }
}
