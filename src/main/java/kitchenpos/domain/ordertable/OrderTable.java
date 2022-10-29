package kitchenpos.domain.ordertable;

import kitchenpos.domain.OrderStatus;

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

    public void setId(final Long id) {
        this.id = id;
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

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("그룹이 있다면 주문 테이블의 비운 상태를 수정할 수 없습니다.");
        }

        if (orderStatus != null && (isCooking() || isMeal())) {
            throw new IllegalArgumentException("조리중이거나 식사중이면 주문 테이블의 비운 상태를 수정할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeOrderStatus(final String orderStatus) {
        this.orderStatus = OrderStatus.from(orderStatus);
    }

    public boolean isCooking() {
        return orderStatus.isCooking();
    }


    public boolean isMeal() {
        return orderStatus.isMeal();
    }
}
