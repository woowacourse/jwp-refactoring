package kitchenpos.domain.table;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeToEmptyTable() {
        this.empty = true;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateInputNumberOfGuests(numberOfGuests);
        validateOrderTableIsNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateInputNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableIsNotEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGrouped() {
        return Objects.nonNull(this.tableGroupId);
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
