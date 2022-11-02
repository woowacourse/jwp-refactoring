package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.order.Order;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private TableStatus status;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(final Long id) {
        this.id = id;
    }

    public OrderTable(final TableStatus status) {
        this(null, null, status, new ArrayList<>());
    }

    public OrderTable(final TableStatus status, final List<Order> orders) {
        this(null, null, status, orders);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final TableStatus status, final List<Order> orders) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.status = status;
        this.orders = orders;
    }

    public void changeEmpty(final boolean empty) {
        validateUngrouped();
        validateAllOrderCompleted();
        status.changeEmpty(empty);
    }

    private void validateUngrouped() {
        if (isGrouped()) {
            throw new DomainLogicException(CustomError.TABLE_ALREADY_GROUPED_ERROR);
        }
    }

    public void changeGuestNumber(final int number) {
        status.changeGuestNumber(number);
    }

    public void changeTableGroup(final TableGroup group) {
        this.tableGroup = group;
    }

    public void validateAllOrderCompleted() {
        for (Order order : orders) {
            validateOrderCompleted(order);
        }
    }

    private void validateOrderCompleted(final Order order) {
        if (!order.isCompleted()) {
            throw new DomainLogicException(CustomError.UNCOMPLETED_ORDER_IN_TABLE_ERROR);
        }
    }

    public void ungroup() {
        this.tableGroup = null;
        this.status.changeEmpty(false);
    }

    public void addOrder(final Order order) {
        validateNotEmpty();
        order.setTable(this);
        this.orders.add(order);
    }

    private void validateNotEmpty() {
        if (status.isEmpty()) {
            throw new DomainLogicException(CustomError.ORDER_TABLE_EMPTY_ERROR);
        }
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public boolean isEmpty() {
        return status.isEmpty();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Long getId() {
        return this.id;
    }

    public int getGuestNumber() {
        return this.status.getNumberOfGuests();
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }
}
