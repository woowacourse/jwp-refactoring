package kitchenpos.domain;

import java.util.Objects;
import kitchenpos.exception.GuestSizeException;

public class OrderTable {
    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(OrderTable orderTable, Long tableGroupId) {
        this.id = orderTable.id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = orderTable.numberOfGuests;
        this.empty = orderTable.empty;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0 || isEmpty()) {
            throw new GuestSizeException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public boolean isNotPossibleTableGrouping() {
        return !isEmpty() || Objects.nonNull(this.tableGroupId);
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
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
