package kitchenpos.table.domain;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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
        this.empty = false;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException("손님 수는 0명 이상이어야 합니다.");
        }
        if (isEmpty()) {
            throw new OrderTableException("비어있지 않은 테이블의 경우 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(OrderTableValidator orderTableValidator, final boolean empty) {
        orderTableValidator.validateChangeEmpty(this);
        setEmpty(empty);
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void grouping(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }
}
