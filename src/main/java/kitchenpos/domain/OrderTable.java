package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(id, tableGroup, numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty, List<Order> orders) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public void changeEmpty(boolean empty) {
        validatesTableGroup();
        validatesOrderStatus();
        this.empty = empty;
    }

    private void validatesTableGroup() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("주문 테이블이 그룹에 속해있습니다. 그룹을 해제해주세요.");
        }
    }

    private void validatesOrderStatus() {
        boolean isCookingOrMeal = orders.stream()
                                        .anyMatch(order -> order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL));
        if (isCookingOrMeal) {
            throw new IllegalArgumentException("주문 상태가 조리중이나 식사중입니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validatesMinusNumberOfGuests(numberOfGuests);
        validatesEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validatesMinusNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경하려는 손님 수는 0이상이어야 합니다.");
        }
    }

    private void validatesEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException("비어있는 테이블의 손님 수를 변경할 수 없습니다.");
        }
    }

    public void changeFull() {
        this.empty = false;
    }

    public void ungroup() {
        validatesOrderStatus();
        this.tableGroup = null;
        this.empty = false;
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

    public List<Order> getOrders() {
        return orders;
    }
}
