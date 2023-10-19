package kitchenpos.order_table.application.dto;

import kitchenpos.order_table.domain.OrderTable;

public class OrderTableQueryResponse {

  private Long id;
  private Long tableGroupId;


  private int numberOfGuests;
  private boolean empty;

  public OrderTableQueryResponse(final Long id, final Long tableGroupId, final int numberOfGuests,
      final boolean empty) {
    this.id = id;
    this.tableGroupId = tableGroupId;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public OrderTableQueryResponse() {
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

  public static OrderTableQueryResponse from(final OrderTable orderTable) {
    return new OrderTableQueryResponse(orderTable.getId(), orderTable.getTableGroupId(),
        orderTable.getNumberOfGuests(),
        orderTable.isEmpty());

  }
}
