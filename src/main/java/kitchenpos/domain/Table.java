package kitchenpos.domain;

public class Table {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public Table() {
        this.id = null;
        this.tableGroupId = null;
        this.numberOfGuests = 0;
        this.empty = true;
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
}
