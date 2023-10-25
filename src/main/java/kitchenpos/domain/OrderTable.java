package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(mappedBy = "orderTable", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private final List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    private OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private OrderTable(
            Long id,
            Long tableGroupId,
            NumberOfGuests numberOfGuests,
            boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(
            int numberOfGuests,
            boolean empty
    ) {
        return new OrderTable(
                NumberOfGuests.from(numberOfGuests),
                empty
        );
    }

    public void addOrder(Order orders) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.orders.add(orders);
        orders.registerOrderTable(this);
    }

    public void registerTableGroup(Long tableGroupId) {
        if (!isEmpty()) {
            throw new IllegalArgumentException("주문이 가능한 주문 테이블은 테이블 그룹에 포함될 수 없습니다.");
        }

        if (isAlreadyContainsTableGroup()) {
            throw new IllegalArgumentException("이미 테이블 그룹에 포함된 주문 테이블은 테이블 그룹에 포함될 수 없습니다.");
        }

        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void breakupTableGroup() {
        boolean canNotBreakup = orders.stream()
                .anyMatch(order -> order.isCooking() || order.isMeal());

        if (canNotBreakup) {
            throw new IllegalArgumentException();
        }

        empty = false;
        tableGroupId = null;
    }

    public boolean isAlreadyContainsTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeEmpty(boolean empty) {
        if (isAlreadyContainsTableGroup()) {
            throw new IllegalArgumentException();
        }

        boolean canNotChangeEmpty = orders.stream()
                .anyMatch(order -> order.isCooking() || order.isMeal());

        if (canNotChangeEmpty) {
            throw new IllegalArgumentException("주문 테이블의 주문 가능 여부를 변경할 수 없습니다.");
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

}
