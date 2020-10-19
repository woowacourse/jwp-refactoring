package kitchenpos.domain;

public class OrderTable {
    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        validateByNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private void validateByNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수가 0보다 작을수 없습니다.");
        }
    }

    public void existTableGroupId() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("테이블 그룹이 존재합니다.");
        }
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

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있는 상태에서는 인원을 변경할 수 없습니다.");
        }
        validateByNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }
}
