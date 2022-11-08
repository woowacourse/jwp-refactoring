package kitchenpos.domain.table;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.service.FindOrderTableInOrderStatusService;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
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
        return Objects.nonNull(getTableGroup());
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

    public void joinTableGroup(final TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
        changeEmpty(false);
    }

    public void validateEmptyAvailable(final FindOrderTableInOrderStatusService findOrderTableInOrderStatusService) {
        final List<OrderStatus> availableStatuses = List.of(OrderStatus.MEAL, OrderStatus.COOKING);
        if (findOrderTableInOrderStatusService.existByOrderStatus(getId(), availableStatuses)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
