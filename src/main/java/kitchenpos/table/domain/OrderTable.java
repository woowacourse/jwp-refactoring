package kitchenpos.table.domain;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public OrderTable ungroup() {
        return new OrderTable(id, null, numberOfGuests, false);
    }

    public OrderTable updateEmpty(final boolean newEmpty) {
        if (hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, numberOfGuests, newEmpty);
    }

    boolean hasTableGroupId() {
        return tableGroupId != null;
    }

    public OrderTable updateNumberOfGuests(final int newNumberOfGuests) {
        if (newNumberOfGuests < 0 || empty) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, newNumberOfGuests, false);
    }

    public void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable init() {
        return new OrderTable(null, null, numberOfGuests, empty);
    }
}
