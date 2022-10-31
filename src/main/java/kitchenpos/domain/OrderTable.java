package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests,
        final boolean empty) {
        validateNumberOfGuests(numberOfGuests);

        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long id) {
        this(id, null, 0, true);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
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

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateNotEmpty();

        this.numberOfGuests = numberOfGuests;
    }

    public void updateEmpty(final boolean empty) {
        validateNotGrouping();

        this.empty = empty;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNotGrouping() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
