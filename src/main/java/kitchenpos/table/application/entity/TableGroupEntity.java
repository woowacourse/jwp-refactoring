package kitchenpos.table.application.entity;

import java.time.LocalDateTime;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

public class TableGroupEntity {

  private Long id;
  private LocalDateTime createdDate;

  public TableGroupEntity(final Long id, final LocalDateTime createdDate) {
    this.id = id;
    this.createdDate = createdDate;
  }

  public TableGroupEntity() {
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public static TableGroupEntity from(final TableGroup tableGroup) {
    return new TableGroupEntity(tableGroup.getId(), tableGroup.getCreatedDate());
  }

  public TableGroup toTableGroup() {
    return new TableGroup(id, createdDate);
  }

  public TableGroup toTableGroup(final OrderTables orderTables) {
    return new TableGroup(id, createdDate, orderTables);
  }
}
