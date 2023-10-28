package kitchenpos.tablegroup.infrastructure.persistence;

import java.time.LocalDateTime;

public class TableGroupEntity {

  private final Long id;
  private final LocalDateTime createdDate;

  public TableGroupEntity(final Long id, final LocalDateTime createdDate) {
    this.id = id;
    this.createdDate = createdDate;
  }

  public TableGroupEntity(final LocalDateTime createdDate) {
    this(null, createdDate);
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }
}
