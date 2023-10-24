package kitchenpos.table.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

public class TableGroupCreateRequest {

  private List<OrderTableReferenceRequest> orderTables;

  public TableGroupCreateRequest(final List<OrderTableReferenceRequest> orderTables) {
    this.orderTables = orderTables;
  }

  public TableGroup toTableGroup(final OrderTables orderTables) {
    return new TableGroup(LocalDateTime.now(), orderTables);
  }

  public TableGroupCreateRequest() {
  }

  public List<OrderTableReferenceRequest> getOrderTables() {
    return orderTables;
  }
}
