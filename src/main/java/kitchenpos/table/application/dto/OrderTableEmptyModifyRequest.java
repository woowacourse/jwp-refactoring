package kitchenpos.table.application.dto;

public class OrderTableEmptyModifyRequest {

  private boolean empty;

  public OrderTableEmptyModifyRequest(final boolean empty) {
    this.empty = empty;
  }

  public OrderTableEmptyModifyRequest() {
  }

  public boolean isEmpty() {
    return empty;
  }
}
