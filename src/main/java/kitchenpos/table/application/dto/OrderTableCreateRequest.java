package kitchenpos.table.application.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableCreateRequest {

  private int numberOfGuests;
  private boolean empty;

  public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public OrderTableCreateRequest() {
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public boolean isEmpty() {
    return empty;
  }

  public OrderTable toOrderTable() {
    return new OrderTable(numberOfGuests, empty);
  }
}
