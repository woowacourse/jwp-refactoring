package kitchenpos.table.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.order.domain.Order;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", nullable = true)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "orderTable")
    private Order order;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void order(Order order) {
        this.order = order;
    }

    public void setEmpty(boolean empty) {
        validateChangeEmpty();
        this.empty = empty;
    }

    private void validateChangeEmpty() {
        if (Objects.nonNull(getTableGroup())) {
            throw new OrderTableException("그룹에 속한 테이블은 비어있음 상태를 변경할 수 없습니다.");
        }
        validateNotCookingOrMealOrder();
    }

    private void validateNotCookingOrMealOrder() {
        if (order == null) {
            return;
        }
        if (Set.of(COOKING.name(), MEAL.name()).contains(order.getOrderStatus())) {
            throw new OrderTableException("조리 혹은 식사중 상태의 테이블의 비어있음 상태는 변경할 수 없습니다.");
        }
    }

    public void grouping(TableGroup tableGroup) {
        if (!isEmpty() || Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException();
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        validateNotCookingOrMealOrder();
        this.tableGroup = null;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException("손님 수는 0명 이상이어야 합니다.");
        }
        if (isEmpty()) {
            throw new OrderTableException("비어있지 않은 테이블의 경우 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
