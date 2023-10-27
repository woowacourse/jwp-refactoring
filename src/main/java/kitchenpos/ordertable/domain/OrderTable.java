package kitchenpos.ordertable.domain;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable() {

    }

    public void changeEmpty(boolean isEmpty) {
        if (this.tableGroupId != null) {
            throw new IllegalArgumentException("단체로 지정된 테이블은 상태를 변경할 수 없습니다.");
        }
        this.empty = isEmpty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }
}
