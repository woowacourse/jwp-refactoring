package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 테이블은 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경할 손님의 수는 0이상이어야 합니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
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
