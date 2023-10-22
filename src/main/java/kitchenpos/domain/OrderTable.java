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

    public OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void groupBy(final TableGroup tableGroup) {
        this.tableGroupId = tableGroup.getId();
        this.empty = false;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
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
