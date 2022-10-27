package kitchenpos.domain;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    public void ungroup() {
        tableGroupId = null;
        empty = false;
    }

    public void changeEmpty(boolean empty) {
        validateEmptyChangeable();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsChangeable(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
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

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    private void validateEmptyChangeable() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("테이블 그룹에 묶여있어 상태를 변경할 수 없습니다.");
        }
    }

    private void validateNumberOfGuestsChangeable(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0 이상이어야 합니다.");
        }

        if (empty) {
            throw new IllegalArgumentException("비어있는 테이블입니다.");
        }
    }
}
