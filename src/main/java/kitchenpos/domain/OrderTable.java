package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void groupBy(Long tableGroupId) {
        if (!empty) {
            throw new IllegalArgumentException("빈 테이블이 아니면 단체 지정할 수 없습니다.");
        }
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("단체 지정된 테이블은 단체 지정을 변경할 수 없습니다.");
        }

        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정된 테이블은 빈 테이블 여부를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("변경할 손님 수는 " + MIN_NUMBER_OF_GUESTS + "명 이상이어야 합니다.");
        }

        if (empty) {
            throw new IllegalArgumentException("빈 테이블이 아닌 경우 손님 수를 변경할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
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
