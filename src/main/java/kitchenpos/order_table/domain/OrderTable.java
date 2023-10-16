package kitchenpos.order_table.domain;

public class OrderTable {

  private Long id;
  private Long tableGroupId;
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
}
