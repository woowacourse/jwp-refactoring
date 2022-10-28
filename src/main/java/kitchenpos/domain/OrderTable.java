package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private Guests numberOfGuests;
    private boolean empty;
    private List<Order> orders;

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(id, tableGroupId, numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty,
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

    private void validateAllOrderCompleted() {
        final var uncompletedOrderExists = orders.stream()
                .anyMatch(order -> order.getOrderStatus() != OrderStatus.COMPLETION);

        if (uncompletedOrderExists) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNotGrouped() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }
    }

    public void validateCanBeGrouped() {
        if (!empty || tableGroupId != null) {
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
        return numberOfGuests.value;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
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
