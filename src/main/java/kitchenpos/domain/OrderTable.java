package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {
    private static final int STANDARD_GUEST_NUMBER = 0;

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable of(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, tableGroupId, numberOfGuests, empty);
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

    public void changeNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < STANDARD_GUEST_NUMBER) {
            throw new IllegalArgumentException("Guest는 0명 미만일 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 그룹 테이블은 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void validateOrderTable() {
        if (this.getTableGroupId() != null) {
            throw new IllegalArgumentException("이미 예약된 테이블이거나 주문 테이블이 비어있지 않습니다.");
        }
    }
}
