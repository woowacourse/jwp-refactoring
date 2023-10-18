package kitchenpos.dao.entity;

public class OrderTableEntity {

  private Long id;
  private Long tableGroupId;
  private int numberOfGuests;
  private boolean empty;

  public OrderTableEntity(
      final Long id,
      final Long tableGroupId,
      final int numberOfGuests,
      final boolean empty
  ) {
    this.id = id;
    this.tableGroupId = tableGroupId;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public OrderTableEntity(
      final Long tableGroupId,
      final int numberOfGuests,
      final boolean empty
  ) {
    this(null, tableGroupId, numberOfGuests, empty);
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
