package kitchenpos.domain;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsIsHigherZero(numberOfGuests);
        validateTableIsNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuestsIsHigherZero(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(String.format("손님의 수는 0명 이하일 수 없습니다. [%s]", numberOfGuests));
        }
    }

    private void validateTableIsNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
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
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
