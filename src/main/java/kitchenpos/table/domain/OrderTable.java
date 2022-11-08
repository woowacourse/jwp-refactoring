package kitchenpos.table.domain;

import java.util.Objects;

public class OrderTable {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;
    private final boolean ordered;

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty, false);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty,
                      final boolean ordered) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.ordered = ordered;
    }

    public OrderTable changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }

        return new OrderTable(id, tableGroupId, numberOfGuests, empty, ordered);
    }

    public OrderTable changeEmpty(final boolean empty) {
        if (ordered) {
            throw new IllegalArgumentException();
        }

        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }

        return new OrderTable(id, tableGroupId, numberOfGuests, empty, ordered);
    }

    public void checkCanGroup() {
        if (!empty || Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void checkCanUnGroup() {
        if (ordered) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable order() {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, true);
    }

    public OrderTable completedOrder() {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, false);
    }

    public OrderTable group(final Long tableGroupId) {
        return new OrderTable(id, tableGroupId, numberOfGuests, false, ordered);
    }

    public OrderTable ungroup() {
        return new OrderTable(id, null, numberOfGuests, false, ordered);
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

    public boolean isOrdered() {
        return ordered;
    }
}
