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

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
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
        this.empty = empty;
    }

    public void changeOrderStatus(final String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public boolean isCooking() {
        return orderStatus.isCooking();
    }


    public boolean isMeal() {
        return orderStatus.isMeal();
    }
}
