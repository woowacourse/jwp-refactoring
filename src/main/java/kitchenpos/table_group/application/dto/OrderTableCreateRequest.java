package kitchenpos.table_group.application.dto;

import kitchenpos.order_table.domain.OrderTable;

public class OrderTableCreateRequest {

  private Long id;

  public OrderTableCreateRequest(final Long id) {
    this.id = id;
  }

  public OrderTableCreateRequest() {
  }

  public Long getId() {
    return id;
  }

  public OrderTable toOrderTable() {
    return new OrderTable(id);
  }
}
