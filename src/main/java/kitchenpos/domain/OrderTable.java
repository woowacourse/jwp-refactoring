package kitchenpos.domain;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validateGuest(numberOfGuests);

        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateGuest(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("게스트는 음수일 수 없습니다.");
        }
    }

    public OrderTable(final Long id, final Long tableGroupId) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = 0;
        this.empty = true;
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

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void attachTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void detachTableGroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
