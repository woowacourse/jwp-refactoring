package kitchenpos.domain.table;

import java.util.List;
import java.util.Objects;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.service.FindOrderTableInOrderStatusService;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public static OrderTable create() {
        return new OrderTable(0, true);
    }

    public boolean isGrouped() {
        return Objects.nonNull(getTableGroupId());
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void enterGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void joinTableGroup(final Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
        changeEmpty(false);
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

    public void validateEmptyAvailable(final FindOrderTableInOrderStatusService findOrderTableInOrderStatusService) {
        final List<OrderStatus> availableStatuses = List.of(OrderStatus.MEAL, OrderStatus.COOKING);
        if (findOrderTableInOrderStatusService.existByOrderStatus(getId(), availableStatuses)) {
            throw new IllegalArgumentException();
        }
    }
}
