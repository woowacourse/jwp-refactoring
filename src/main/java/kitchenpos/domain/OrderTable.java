package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    private OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public static OrderTable create(final int numberOfGuests, final boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 0명 이상이어야 합니다.");
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

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.empty) {
            throw new IllegalArgumentException("빈 테이블의 방문한 손님 수는 변경할 수 없습니다.");
        }
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("그룹 지정된 테이블은 빈 테이블로 변경할 수 없습니다.");
        }
        this.empty = empty;
    }
}
