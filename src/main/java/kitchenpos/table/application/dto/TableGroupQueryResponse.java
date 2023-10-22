package kitchenpos.table.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.TableGroup;

public class TableGroupQueryResponse {

  private Long id;
  private LocalDateTime createdDate;
  private List<OrderTableQueryResponse> orderTables;

  public TableGroupQueryResponse(final Long id, final LocalDateTime createdDate,
      final List<OrderTableQueryResponse> orderTables) {
    this.id = id;
    this.createdDate = createdDate;
    this.orderTables = orderTables;
  }

  public TableGroupQueryResponse() {
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public List<OrderTableQueryResponse> getOrderTables() {
    return orderTables;
  }

  public static TableGroupQueryResponse from(final TableGroup tableGroup) {
    final List<OrderTableQueryResponse> orderTables = OrderTableQueryResponse.from(
        tableGroup.getOrderTables());
    return new TableGroupQueryResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
        orderTables);
  }
}
