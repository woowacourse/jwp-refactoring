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
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroupId = tableGroupId;
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(tableGroupId, numberOfGuests, empty);
        this.id = id;
    }

    public void validateEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }
    }

    public void changeEmpty(final boolean empty) {
        validateTableGroupId();
        this.empty = empty;
    }

    private void validateTableGroupId() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("TableGroupId가 있습니다.");
        }
    }

    public void changeNumberOfGuest(final int numberOfGuests) {
        validateEmpty();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0 이상이어야합니다.");
        }
    }

    public void validateToCreate() {
        if (isTableNotEmptyOrNotNull()) {
            throw new IllegalArgumentException("저장할 table은 empty 샹태가 아니거나 다른 table group에 포함되서는 안됩니다.");
        }
    }

    private boolean isTableNotEmptyOrNotNull() {
        return !isEmpty() || Objects.nonNull(tableGroupId);
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
}
