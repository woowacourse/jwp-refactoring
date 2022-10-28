package kitchenpos.domain;

import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableRequest;

public class OrderTable {
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

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable from(OrderTableRequest orderTableRequest) {
        return new OrderTable(orderTableRequest.getId(), null, 0, true);
    }

    public static OrderTable from(OrderTableCreateRequest orderTableCreateRequest) {
        return new OrderTable(null, null, orderTableCreateRequest.getNumberOfGuests(),
                orderTableCreateRequest.isEmpty());
    }

    public void changeEmpty(boolean empty) {
        validateExistTableGroupId();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0보다 작을 수 없습니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("주문을 등록할 수 없는 주문 테이블입니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void validateExistTableGroupId() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("테이블 그룹을 가지고 있을 경우 상태를 변경할 수 없습니다.");
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
