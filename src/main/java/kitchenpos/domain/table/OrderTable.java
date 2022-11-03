package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Entity;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

public class OrderTable implements Entity {

    private Long id;
    private Long tableGroupId;
    private Guests numberOfGuests;
    private boolean empty;
    private List<Order> orders;

    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this(null, null, numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        this(id, tableGroupId, numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty,
                      final List<Order> orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new Guests(numberOfGuests);
        this.empty = empty;
        this.orders = orders;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new Guests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmptyTo(final boolean status) {
        validateNotGrouped();
        validateAllOrderCompleted();
        this.empty = status;
    }

    private void validateNotGrouped() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAllOrderCompleted() {
        if (!isAllOrderCompleted()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isAllOrderCompleted() {
        return orders.stream()
                .noneMatch(order -> order.getOrderStatus() != OrderStatus.COMPLETION);
    }

    public boolean canBeUngrouped() {
        return isAllOrderCompleted();
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public boolean canBeGrouped() {
        return empty && tableGroupId == null;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public void validateOnCreate() {
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

    public int getNumberOfGuests() {
        return numberOfGuests.value;
    }

    public boolean isEmpty() {
        return empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

    private static class Guests {

        private final int value;

        private Guests(final int value) {
            validateAtLeastZero(value);
            this.value = value;
        }

        private void validateAtLeastZero(final int value) {
            if (value < 0) {
                throw new IllegalArgumentException();
            }
        }
    }
}
