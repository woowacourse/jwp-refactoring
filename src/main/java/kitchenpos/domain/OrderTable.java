package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;
    private List<Order> orders;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty, Collections.emptyList());
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(id, tableGroupId, numberOfGuests, empty, Collections.emptyList());
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty,
                      final List<Order> orders) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public void updateEmpty(final boolean empty) {
        validateGrouped();
        validateOrderStatus();
        this.empty = empty;
    }

    public void ungroup() {
        validateUngroup();
        tableGroupId = null;
        empty = false;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatus() {
        if (orders.isEmpty()) {
            return;
        }

        orders.stream()
                .map(Order::getOrderStatus)
                .filter(OrderStatus::isCompletion)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateGrouped() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
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

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

    private void validateUngroup() {
        if (orders.isEmpty()) {
            return;
        }
        final boolean notCompletedOrder = orders.stream()
                .anyMatch(it -> !OrderStatus.isCompletion(it.getOrderStatus()));
        if (notCompletedOrder) {
            throw new IllegalArgumentException();
        }
    }
}
