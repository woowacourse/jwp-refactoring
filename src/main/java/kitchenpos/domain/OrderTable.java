package kitchenpos.domain;

public class OrderTable {

    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable ofNew(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void joinGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void acceptGuests(final int numberOfGuests) {
        acceptGuests();
        this.numberOfGuests = numberOfGuests;
    }

    public void acceptGuests() {
        this.empty = false;
    }

    public void clear() {
        this.empty = true;
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
