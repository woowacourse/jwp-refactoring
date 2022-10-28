package kitchenpos.domain;

public class OrderTable {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);

        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public void validateNotInTableGroup() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }
    }

    public void validateNotEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        };
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
