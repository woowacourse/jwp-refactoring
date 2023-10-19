package kitchenpos.domain.order;

import kitchenpos.exception.OrderTableException;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.PERSIST)
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

    private void validateOrderStatusWhenChangeEmpty(Order order) {
        if (order.getOrderStatus() == OrderStatus.COOKING || order.getOrderStatus() == OrderStatus.MEAL) {
            throw new OrderTableException("주문테이블에 속한 주문이 요리중 또는 식사중이므로 상태를 변경할 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(GuestNumber numberOfGuests) {
        if (empty) {
            throw new OrderTableException("주문테이블이 주문을 할 수 없는 상태라 게스트 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void addOrders(List<Order> orders) {
        for (Order order : orders) {
            order.changeOrderTable(this);
        }
        this.orders.addAll(orders);
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
