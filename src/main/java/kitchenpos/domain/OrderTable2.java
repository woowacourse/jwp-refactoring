package kitchenpos.domain;

public class OrderTable2 {

  private Long id;
  private TableGroup2 tableGroup;
  private int numberOfGuests;
  private boolean empty;

  public OrderTable2(
      final Long id,
      final TableGroup2 tableGroup,
      final int numberOfGuests,
      final boolean empty
  ) {
    this.id = id;
    this.tableGroup = tableGroup;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public OrderTable2(
      final TableGroup2 tableGroup,
      final int numberOfGuests,
      final boolean empty
  ) {
    this(null, tableGroup, numberOfGuests, empty);
  }

  public OrderTable2(
      final int numberOfGuests,
      final boolean empty
  ) {
    this(null, null, numberOfGuests, empty);
  }

  public boolean isNotBelongTableGroup() {
    return tableGroup != null;
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

  public TableGroup2 getTableGroup() {
    return tableGroup;
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public boolean isEmpty() {
    return empty;
  }
}
