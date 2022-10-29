package kitchenpos.domain;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;
    private OrderStatus orderStatus;

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty,
                      final OrderStatus orderStatus) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orderStatus = orderStatus;
    }

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        this(id, tableGroupId, numberOfGuests, empty, null);
    }

    public OrderTable(final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty, null);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void changeTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
        }

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("주문 테이블의 손님 수는 음수로 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("그룹이 있다면 주문 테이블의 비운 상태를 수정할 수 없습니다.");
        }

        if (orderStatus != null && (isCooking() || isMeal())) {
            throw new IllegalArgumentException("조리중이거나 식사중이면 주문 테이블의 비운 상태를 수정할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isCooking() {
        return orderStatus.isCooking();
    }


    public boolean isMeal() {
        return orderStatus.isMeal();
    }
}
