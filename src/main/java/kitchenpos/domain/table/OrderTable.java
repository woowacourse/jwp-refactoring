package kitchenpos.domain.table;

import kitchenpos.domain.order.Orders;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    @Embedded
    private Orders orders;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroupId = null;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean empty) {
        if (this.tableGroupId != null) {
            throw new IllegalArgumentException();
        }
        if (orders.inCookingOrMeal()) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public boolean hasCookingOrMealOrder() {
        return orders.inCookingOrMeal();
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests <= 0) {
            throw new IllegalArgumentException();
        }
        if (this.empty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public boolean hasTableGroup() {
        return this.tableGroupId != null;
    }

    public boolean hasNoTableGroup() {
        return this.tableGroupId == null;
    }

    public void unGroup() {
        this.tableGroupId = null;
        this.empty = true;
    }

    public boolean isNotEmpty() {
        return !this.empty;
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

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public void makeFull() {
        this.empty = false;
    }
}
