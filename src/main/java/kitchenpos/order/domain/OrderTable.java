package kitchenpos.order.domain;

import kitchenpos.table.domain.TableGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {

    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();

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

    public OrderTable(final Long id,
                      final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void placeOrder(final Order order) {
        validateAbleToOrder();
        orders.add(order);
    }

    private void validateAbleToOrder() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }
    }

    public void group(final TableGroup tableGroup) {
        validateAbleToGroup();
        changeEmpty(false);
        this.tableGroup = tableGroup;
    }

    private void validateAbleToGroup() {
        if (!empty) {
            throw new IllegalArgumentException("이미 주문 상태인 테이블을 단체로 지정할 수 없습니다.");
        }
        if (tableGroup != null) {
            throw new IllegalArgumentException("이미 단체에 속한 테이블을 단체로 지정할 수 없습니다.");
        }
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 당 인원 수는 음수가 될 수 없습니다.");
        }
    }

    public void unGroup() {
        validateAbleToUnGroup();
        this.tableGroup = null;
        changeEmpty(false);
    }

    private void validateAbleToUnGroup() {
        if (tableGroup == null) {
            throw new IllegalArgumentException("테이블이 그룹에 이미 속해있지 않습니다.");
        }
        if (hasAnyOrderInProgress()) {
            throw new IllegalArgumentException("이미 조리 또는 식사 중인 주문이 존재하면 테이블을 나눌 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 인원 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        validateAbleToChangeEmpty();
        this.empty = empty;
    }

    private void validateAbleToChangeEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체로 지정된 테이블을 비울 수 없습니다.");
        }
        if (hasAnyOrderInProgress()) {
            throw new IllegalArgumentException("조리 또는 식사 중인 테이블을 비울 수 없습니다.");
        }
    }

    private boolean hasAnyOrderInProgress() {
        return orders.stream()
                .anyMatch(Order::isInProgress);
    }

    public List<Order> getOrders() {
        return orders;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }
}
