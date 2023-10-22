package kitchenpos.table_group.domain;

import java.time.LocalDateTime;

public class TableGroup {

  private final Long id;
  private final LocalDateTime createdDate;
  private final OrderTables orderTables;

  public TableGroup(final Long id, final LocalDateTime createdDate,
      final OrderTables orderTables) {
    this.id = id;
    this.createdDate = createdDate;
    this.orderTables = orderTables;
  }

  public TableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
    this(null, createdDate, orderTables);
  }

  public TableGroup(final Long id, final LocalDateTime createdDate) {
    this(id, createdDate, new OrderTables());
  }

  public TableGroup(final LocalDateTime createdDate) {
    this(null, createdDate, new OrderTables());
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public OrderTables getOrderTables() {
    return orderTables;
  }
}
