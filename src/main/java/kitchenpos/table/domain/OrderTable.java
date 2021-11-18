package kitchenpos.table.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @JoinColumn(name = "table_group_id")
    @ManyToOne
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Orders orders;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests,
                      final boolean empty) {
        this(id, tableGroup, new NumberOfGuests(numberOfGuests), empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup,
                      final NumberOfGuests numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new Orders();
    }

    public void changeEmpty(final boolean empty) {
        validateToChangeEmpty();
        this.empty = empty;
    }

    private void validateToChangeEmpty() {
        if (isIncludedInGroup()) {
            throw new IllegalArgumentException("단체 지정이 된 테이블은 빈 상태를 수정할 수 없습니다.");
        }
        if (orders.containsOrderStatusIn(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public boolean containsOrderStatusIn(final List<OrderStatus> orderStatuses) {
        return orders.containsOrderStatusIn(orderStatuses);
    }

    private boolean isIncludedInGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateToChangeNumberOfGuests();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateToChangeNumberOfGuests() {
        if (empty) {
            throw new IllegalArgumentException("빈 상태의 테이블의 손님 수를 수정할 수 없습니다.");
        }
    }

    public void addOrder(final Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
        }
    }

    public void changeTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public List<Order> getOrders() {
        return orders.getElements();
    }

    public boolean isEmpty() {
        return empty;
    }
}
