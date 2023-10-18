package kitchenpos.domain;

public class OrderTable2 {

  private Long id;
  private Long tableGroupId;
  private int numberOfGuests;
  private boolean empty;

  public OrderTable2(
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

  public OrderTable2(
      final Long tableGroupId,
      final int numberOfGuests,
      final boolean empty
  ) {
    this(null, tableGroupId, numberOfGuests, empty);
  }

  public OrderTable2(
      final int numberOfGuests,
      final boolean empty
  ) {
    this(null, null, numberOfGuests, empty);
  }

  public boolean isNotBelongTableGroup() {
    return tableGroupId != null;
  }

  public void changeEmpty(final boolean empty) {
    this.empty = empty;
  }

  public void changeNumberOfGuests(final int numberOfGuests) {
    if (numberOfGuests < 0) {
      throw new IllegalArgumentException();
    }

    this.numberOfGuests = numberOfGuests;
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
