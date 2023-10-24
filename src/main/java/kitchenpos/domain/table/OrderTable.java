package kitchenpos.domain.table;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_EMPTY_COOKING_OR_MEAL;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.order.Order;
import kitchenpos.exception.table.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable", fetch = EAGER)
    private List<Order> orders;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable(Long id, TableGroup tableGroup, List<Order> orders, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable emptyTable() {
        return new OrderTable(0, true);
    }

    public boolean hasOrderOfCookingOrMeal() {
        return orders.stream()
                .anyMatch(Order::isCookingOrMeal);
    }

    public void ungroup() {
        tableGroup = null;
        empty = false;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new OrderTableException(CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE);
        }
        if (orders.stream().anyMatch(Order::isCookingOrMeal)) {
            throw new OrderTableException(CAN_NOT_CHANGE_EMPTY_COOKING_OR_MEAL);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException(NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE);
        }
        if (empty) {
            throw new OrderTableException(CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Long id() {
        return id;
    }

    public TableGroup tableGroup() {
        return tableGroup;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean empty() {
        return empty;
    }
}
