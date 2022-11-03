package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {
    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 고객 수는 0 이상이어야 한다.");
        }
    }

    public OrderTable validateTableIsFull() {
        if (isEmpty()) {
            throw new IllegalArgumentException("테이블은 차있어야 한다.");
        }
        return this;
    }

    public void placeTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public OrderTable placeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 고객 수는 0 이상이어야 한다.");
        }
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTable changeEmpty(final boolean empty) {
        validateNoGroup();
        this.empty = empty;
        return this;
    }

    private void validateNoGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블은 단체지정이 없어야 한다.");
        }
    }

    public void changeToFull() {
        this.empty = false;
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
