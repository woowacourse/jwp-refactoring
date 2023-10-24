package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable() {
        this(null, null, 0, true);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
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
        return numberOfGuests.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable)) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id)
                && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
