package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }

        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }

        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public void checkCanGroup() {
        if (!empty || Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable group(final Long tableGroupId) {
        return new OrderTable(id, tableGroupId, numberOfGuests, false);
    }

    public OrderTable ungroup() {
        return new OrderTable(id, null, numberOfGuests, false);
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
