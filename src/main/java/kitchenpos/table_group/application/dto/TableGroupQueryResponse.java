package kitchenpos.table_group.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order_table.application.dto.OrderTableQueryResponse;
import kitchenpos.table_group.domain.TableGroup;

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
    final List<OrderTableQueryResponse> orderTables = tableGroup.getOrderTables()
        .stream()
        .map(OrderTableQueryResponse::from)
        .collect(Collectors.toList());
    
    return new TableGroupQueryResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
        orderTables);
  }
}
