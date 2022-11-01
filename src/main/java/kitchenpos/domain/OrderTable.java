package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long id, final Long tableGroupId,
                      final int numberOfGuests, final boolean empty) {
        validateEmpty();
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
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

    public void validateHasGroupId() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderTableSize() {
        if (!this.isEmpty() || Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
