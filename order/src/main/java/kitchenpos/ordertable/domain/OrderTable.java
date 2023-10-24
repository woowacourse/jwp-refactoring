package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class OrderTable {
    @Id
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;
    @Embedded.Empty
    private Orders orders;

    private OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty, Orders.createEmptyOrders());
    }


    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty, Orders orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }

        if (orders.checkIncompleteOrders()) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (empty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void add(Order order) {
        if (empty) {
            throw new IllegalArgumentException();
        }

        orders.add(order);
    }

    public boolean canGroup() {
        return empty && tableGroupId == null;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        if (orders.checkIncompleteOrders()) {
            throw new IllegalArgumentException();
        }

        this.tableGroupId = null;
        this.empty = false;
    }

    public Order getNewOrder() {
        return orders.getNewOrder();
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
}
