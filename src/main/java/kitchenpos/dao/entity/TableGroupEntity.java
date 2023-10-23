package kitchenpos.dao.entity;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;

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
