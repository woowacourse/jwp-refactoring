package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public Long getId() {
        return id;
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

    public boolean isEmpty() {
        return empty;
    }
}
