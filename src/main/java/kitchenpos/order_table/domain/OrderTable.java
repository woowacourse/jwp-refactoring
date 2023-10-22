package kitchenpos.order_table.domain;

import java.util.Objects;

public class OrderTable {

  private final Long id;
  private final Long tableGroupId;
  private int numberOfGuests;
  private boolean empty;

  public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests,
      final boolean empty) {
    this.id = id;
    this.tableGroupId = tableGroupId;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public OrderTable(final Long id, final Long tableGroupId) {
    this(id, tableGroupId, 0, true);
  }

  public OrderTable(final int numberOfGuests, final boolean empty) {
    this(null, null, numberOfGuests, empty);
  }

  public OrderTable(final Long id) {
    this(id, null, 0, true);
  }

  public Long getId() {
    return id;
  }

  public Long getTableGroupId() {
    return tableGroupId;
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public boolean isEmpty() {
    return empty;
  }

  public boolean hasTableGroup() {
    return Objects.nonNull(tableGroupId);
  }

  public void updateNumberOfGuests(final int numberOfGuests) {
    this.numberOfGuests = numberOfGuests;
  }

  public void updateEmpty(final boolean empty) {
    this.empty = empty;
  }
}
