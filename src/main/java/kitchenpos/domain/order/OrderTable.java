package kitchenpos.domain.order;

import static javax.persistence.FetchType.LAZY;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;

import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfGuests;

    private boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders;

    @JoinColumn(name = "table_group_id")
    @ManyToOne(fetch = LAZY)
    private TableGroup tableGroup;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests,
                      boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests,
                      boolean empty,
                      TableGroup tableGroup) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public OrderTable(Long id,
                      int numberOfGuests,
                      boolean empty,
                      TableGroup tableGroup) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public void clearKeys() {
        this.id = null;
        this.tableGroup = null;
    }

    public void mapTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void validateChangeEmptyStatus() {
        validateUnGroup();
        validateCompletionStatus();
    }

    public void ungroup() {
        tableGroup = null;
    }

    public void changeStatusNotEmpty() {
        empty = false;
    }

    public void validateChangeNumberOfGuests() {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있는 경우 손님 수를 변경할 수 없습니다.");
        }
    }

    private void validateUnGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("주문 테이블이 단체 지정되었을 경우 상태를 변경할 수 없습니다.");
        }
    }

    private void validateCompletionStatus() {
        if (isOrderInMealOrCooking()) {
            throw new IllegalArgumentException("주문 테이블이 계산 완료되었을 경우에만 상태를 변경할 수 있습니다.");
        }
    }

    private boolean isOrderInMealOrCooking() {
        return orders.stream()
                .anyMatch(this::containsInMealOrCooking);
    }

    private boolean containsInMealOrCooking(Order order) {
        return List.of(COOKING, MEAL).contains(order.getOrderStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmptyStatus(boolean empty) {
        this.empty = empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
