package kitchenpos.table.domain;

import static kitchenpos.table.domain.OrderStatus.NO_ORDER;

import java.util.Objects;
import org.springframework.data.annotation.Id;

public class OrderTable {

    @Id
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;
    private final OrderStatus orderStatus;

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty, NO_ORDER);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty, final OrderStatus orderStatus) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orderStatus = orderStatus;
    }

    public OrderTable group(final Long tableGroupId) {
        return new OrderTable(id, tableGroupId, numberOfGuests, false, orderStatus);
    }

    public OrderTable changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, orderStatus);
    }

    public OrderTable changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
        if (orderStatus.existCustomer()) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, orderStatus);
    }

    public void verifyCanGroup() { // 비어있고, 단체지정 되어있지 않아야함
        final boolean canGroup = empty && tableGroupId == null;
        if (!canGroup) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable ungroup() {
        if (orderStatus.existCustomer()) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, null, numberOfGuests, false, orderStatus);
    }

    public OrderTable changeOrderStatus(final OrderStatus orderStatus) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, orderStatus);
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
