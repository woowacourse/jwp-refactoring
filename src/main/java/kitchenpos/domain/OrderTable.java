package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private static final int MIN_GUESTS = 0;

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuest(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private void validateNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < MIN_GUESTS) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isPartOfTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public OrderTable changeNumberOfGuest(final int numberOfGuests) {
        validateEmpty();
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    private void validateEmpty() {
        if (empty) {
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
}
