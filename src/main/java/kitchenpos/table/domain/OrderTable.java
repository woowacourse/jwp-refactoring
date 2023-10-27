package kitchenpos.table.domain;

import java.util.Objects;

import static kitchenpos.table.domain.NumberOfGuests.DEFAULT_NUMBER_OF_GUESTS;

public class OrderTable {

    private final Long id;
    private final Long tableGroupId;
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final NumberOfGuests numberOfGuests,
                      final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long id, final Long tableGroupId) {
        this(id, tableGroupId, DEFAULT_NUMBER_OF_GUESTS, true);
    }

    public OrderTable(final NumberOfGuests numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id) {
        this(id, null, DEFAULT_NUMBER_OF_GUESTS, true);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void updateNumberOfGuests(final NumberOfGuests numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNotEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void updateEmpty(final boolean empty) {
        validateHasNotTableGroup();
        this.empty = empty;
    }

    private void validateHasNotTableGroup() {
        if (hasTableGroup()) {
            throw new IllegalArgumentException();
        }
    }
}
