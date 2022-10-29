package kitchenpos.domain.table;

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

    public static OrderTable create() {
        return new OrderTable(null, null, 0, true);
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void enterGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void joinTableGroup(final Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
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
