package kitchenpos.table_group.application.dto;

import java.util.List;

public class TableGroupCreateRequest {

  private List<OrderTableCreateRequest> orderTables;

  public TableGroupCreateRequest(final List<OrderTableCreateRequest> orderTables) {
    this.orderTables = orderTables;
  }

  public TableGroupCreateRequest() {
  }

  public List<OrderTableCreateRequest> getOrderTables() {
    return orderTables;
  }

}
