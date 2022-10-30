package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validatePositive(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private void validatePositive(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable ungroup() {
        return new OrderTable(id, null, numberOfGuests, false);
    }

    public OrderTable updateNumberOfGuests(final int numberOfGuests) {
        validateEmpty();
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    private void validateEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateHasTableGroup() {
        if (hasTableGroup()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public OrderTable(final Long id) {
        this.id = id;
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

    public void updateEmpty(final boolean empty) {
        validateHasTableGroup();
        this.empty = empty;
    }
}
