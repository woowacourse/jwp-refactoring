package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.exception.TableGroupException;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Embedded
    private GuestNumber numberOfGuests;

    private Boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(GuestNumber numberOfGuests, Boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(Long id, GuestNumber numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        if (isNotEmpty()) {
            throw new TableGroupException("주문 테이블이 주문이 가능한 상태여서 테이블 그룹을 생성할 수 없습니다.");
        }
        if (existsTableGroup()) {
            throw new TableGroupException("주문 테이블이 이미 테이블 그룹에 속해 있어 테이블 그룹을 생성할 수 없습니다.");
        }
        empty = Boolean.FALSE;
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(Boolean empty) {
        if (existsTableGroup()) {
            throw new OrderTableException("주문테이블이 테이블 그룹에 속해 상태를 변경할 수 없습니다.");
        }
        for (Order order : orders) {
            validateOrderStatusWhenChangeEmpty(order);
        }
        this.empty = empty;
    }

    private boolean existsTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    private boolean isNotEmpty() {
        return !this.empty;
    }

    private void validateOrderStatusWhenChangeEmpty(Order order) {
        if (!order.isCompleted()) {
            throw new OrderTableException("주문테이블에 속한 주문이 요리중 또는 식사중이므로 상태를 변경할 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(GuestNumber numberOfGuests) {
        if (empty) {
            throw new OrderTableException("주문테이블이 주문을 할 수 없는 상태라 게스트 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void addOrder(Order order) {
        if (empty) {
            throw new OrderTableException("주문테이블이 주문을 할 수 없는 상태라 주문을 추가할 수 없습니다.");
        }
        order.changeOrderTable(this);
        orders.add(order);
    }

    public void ungroup() {
        for (Order order : orders) {
            validateOrderStatus(order);
        }
        empty = false;
        tableGroup = null;
    }

    public void validateOrderStatus(Order order) {
        if (!order.isCompleted()) {
            throw new OrderTableException("주문테이블에 속한 주문이 요리중 또는 식사중이므로 상태를 변경할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public Boolean getEmpty() {
        return empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
