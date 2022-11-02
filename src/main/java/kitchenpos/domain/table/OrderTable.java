package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.order.Order;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private TableStatus status;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(final Long id) {
        this(id, null, null, new ArrayList<>());
    }

    public OrderTable(final Long tableGroupId, final TableStatus status) {
        this(null, tableGroupId, status, new ArrayList<>());
    }

    public OrderTable(final TableStatus status) {
        this(null, null, status, new ArrayList<>());
    }

    public OrderTable(final TableStatus status, final List<Order> orders) {
        this(null, null, status, orders);
    }

    public OrderTable(final Long id, final Long tableGroupId, final TableStatus status, final List<Order> orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
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

    public void changeTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
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
        this.tableGroupId = null;
        this.status.changeEmpty(false);
    }

    public void validateNotEmpty() {
        if (status.isEmpty()) {
            throw new DomainLogicException(CustomError.ORDER_TABLE_EMPTY_ERROR);
        }
    }

    public boolean isGrouped() {
        return this.tableGroupId != null;
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

    public Long getTableGroupId() {
        return this.tableGroupId;
    }
}
