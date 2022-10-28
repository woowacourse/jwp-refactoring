package kitchenpos.domain;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable ofNullId(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, tableGroupId, numberOfGuests, empty);
    }

    public void updateTableGroupId(final Long newTableGroupId) {
        this.tableGroupId = newTableGroupId;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void updateEmpty(final boolean isEmpty) {
        this.empty = isEmpty;
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
