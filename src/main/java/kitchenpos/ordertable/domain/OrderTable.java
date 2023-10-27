package kitchenpos.ordertable.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    private static final String COOKING = OrderStatus.COOKING.name();
    private static final String MEAL = OrderStatus.MEAL.name();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfGuests;

    private boolean empty;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty, List<Order> orders) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroupId = tableGroupId;
    }

    public Order order(List<OrderLineItem> items) {
        if (empty) {
            throw new IllegalArgumentException("해당 테이블은 비어있습니다.");
        }
        return new Order(null, OrderStatus.COOKING.name(), LocalDateTime.now(), items);
    }

    public void changeEmpty(boolean empty) {
        validateOrderStatus();
        this.empty = empty;
    }

    private void validateOrderStatus() {
        orders.stream()
            .filter(order -> COOKING.equals(order.getOrderStatus()) || MEAL.equals(order.getOrderStatus()))
            .findAny()
            .ifPresent(ignore -> new IllegalArgumentException("조리 중이거나 식사 중인 주문이 있다면 빈 테이블로 만들 수 없습니다."));
    }

    public void changeNumbersOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 손님 수를 바꿀 수 없습니다");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0보다 커야합니다");
        }
    }

    public void clearing() {
        this.empty = true;
    }

    public void filling() {
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
