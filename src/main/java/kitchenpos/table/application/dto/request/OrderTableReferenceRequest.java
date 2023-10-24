package kitchenpos.table.application.dto.request;

import kitchenpos.table.domain.OrderTable;

public class OrderTableReferenceRequest {

  private Long id;

  public OrderTableReferenceRequest(final Long id) {
    this.id = id;
  }

  public OrderTableReferenceRequest() {
  }

  public Long getId() {
    return id;
  }

  public OrderTable toOrderTable() {
    return new OrderTable(id);
  }
}
