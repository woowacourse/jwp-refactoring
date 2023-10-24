package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.domain.common.NumberOfGuests;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidOrderStatusCompletionException;
import kitchenpos.domain.exception.InvalidTableGroupException;
import kitchenpos.domain.order.Order;

@Entity
public class OrderTable {

    private static final TableGroup UNGROUP_TABLE_GROUP = null;
    private static final boolean UNGROUP_EMPTY = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToOne(mappedBy = "orderTable")
    private Order order;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void initOrder(final Order order) {
        this.order = order;
    }

    public void group(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        validateOrderStatus();

        this.tableGroup = UNGROUP_TABLE_GROUP;
        this.empty = UNGROUP_EMPTY;
    }

    private void validateOrderStatus() {
        if (!order.isCompletion()) {
            throw new InvalidOrderStatusCompletionException();
        }
    }

    public void changeEmptyStatus(final List<Order> orders, final boolean empty) {
        if (this.tableGroup != null) {
            throw new InvalidTableGroupException();
        }

        for (final Order order : orders) {
            if (!order.isCompletion()) {
                throw new InvalidOrderStatusCompletionException();
            }
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.empty) {
            throw new InvalidEmptyOrderTableException();
        }

        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }

        final OrderTable targetOrderTable = (OrderTable) target;

        return Objects.equals(getId(), targetOrderTable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
