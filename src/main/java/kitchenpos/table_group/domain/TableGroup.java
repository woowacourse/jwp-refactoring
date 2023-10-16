package kitchenpos.table_group.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.order_table.domain.OrderTable;

public class TableGroup {

  private Long id;
  private LocalDateTime createdDate;
  private List<OrderTable> orderTables;

  public TableGroup(final Long id, final LocalDateTime createdDate,
      final List<OrderTable> orderTables) {
    this.id = id;
    this.createdDate = createdDate;
    this.orderTables = orderTables;
  }

  public TableGroup(final Long id, final LocalDateTime createdDate) {
    this(id, createdDate, new ArrayList<>());
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public List<OrderTable> getOrderTables() {
    return orderTables;
  }
}
