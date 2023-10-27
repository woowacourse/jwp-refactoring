package kitchenpos.ordertable.domain;

import java.time.LocalDateTime;
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
import kitchenpos.common.exception.CannotChangeEmptyException;
import kitchenpos.common.exception.InvalidGuestNumberException;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidUnGroupException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

@Entity
public class OrderTable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "empty", nullable = false)
    private Boolean empty;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_table_id", nullable = false, updatable = false)
    private List<Order> orders;

    protected OrderTable() {
    }

    public OrderTable(final Integer numberOfGuests, final Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final Integer numberOfGuests, final Boolean empty) {
        validateCanBeEmpty(numberOfGuests, empty);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
        this.orders = new ArrayList<>();
    }

    public Order order(final List<OrderLineItem> orderLineItems, final LocalDateTime orderedTime) {
        if (empty) {
            throw new InvalidOrderException("주문 테이블이 비어있습니다.");
        }
        final Order order = new Order(orderLineItems, orderedTime, id);
        this.orders.add(order);
        return order;
    }

    private void validateCanBeEmpty(final Integer numberOfGuests, final Boolean empty) {
        if (numberOfGuests > 0 && empty) {
            throw new InvalidGuestNumberException("손님 수가 1명 이상이면 빈 테이블이 될 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(final Integer numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new InvalidGuestNumberException("빈 테이블에는 손님 수를 변경할 수 없습니다.");
        }
    }

    public void changeEmpty(final Boolean empty) {
        if (hasTableGroup()) {
            throw new CannotChangeEmptyException("그룹 지정된 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }
        if (!orders.isEmpty() && orders.stream()
                                       .anyMatch(Order::isCookingOrMeal)) {
            throw new CannotChangeEmptyException("조리 또는 식사중인 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }
        validateCanBeEmpty(numberOfGuests.getValue(), empty);
        this.empty = empty;
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public void group(final Long tableGroupId) {
        changeEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        if (tableGroupId == null) {
            throw new InvalidUnGroupException("그룹 지정되지 않은 테이블은 그룹을 해제할 수 없습니다.");
        }
        if (orders.stream()
                  .anyMatch(Order::isCookingOrMeal)) {
            throw new InvalidUnGroupException("조리 또는 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
    }

    public Boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
