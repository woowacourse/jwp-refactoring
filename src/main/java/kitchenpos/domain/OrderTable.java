package kitchenpos.domain;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private Guests numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests) {
        this.numberOfGuests = new Guests(numberOfGuests);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new Guests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmptyTo(final boolean status) {
        validateNotGrouped();
        this.empty = status;
    }

    private void validateNotGrouped() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = new Guests(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    private static class Guests {

        private final int value;

        private Guests(final int value) {
            validateAtLeastZero(value);
            this.value = value;
        }

        private void validateAtLeastZero(final int value) {
            if (value < 0) {
                throw new IllegalArgumentException();
            }
        }
    }
}
