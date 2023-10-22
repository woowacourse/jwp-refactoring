package kitchenpos.table_group.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table_group.domain.OrderTables;
import kitchenpos.table_group.domain.TableGroup;

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

  public TableGroup toTableGroup(final OrderTables orderTables) {
    return new TableGroup(LocalDateTime.now(), orderTables);
  }
}
