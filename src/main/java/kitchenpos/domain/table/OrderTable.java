package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.order.Order;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @OneToMany(mappedBy = "orderTable", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, new ArrayList<>(), new NumberOfGuests(numberOfGuests), empty);
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, new ArrayList<>(), new NumberOfGuests(numberOfGuests), empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(id, tableGroupId, new ArrayList<>(), new NumberOfGuests(numberOfGuests), empty);
    }

    private OrderTable(
            Long id,
            Long tableGroupId,
            List<Order> orders,
            NumberOfGuests numberOfGuests,
            boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        validateOrderCompleted();
        validateTableGroupNull();
        this.empty = empty;
    }

    private void validateOrderCompleted() {
        if (getOrders().stream().anyMatch(order -> !order.isCompleted())) {
            throw new IllegalStateException("완료되지 않은 주문이 있습니다.");
        }
    }

    private void validateTableGroupNull() {
        if (this.tableGroupId != null) {
            throw new IllegalArgumentException("주문 테이블의 테이블 그룹이 없어야 합니다. : " + tableGroupId);
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있기 때문에 손님의 수를 변경할 수 없습니다");
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void group(Long tableGroupId) {
        validateEmpty();
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    private void validateEmpty() {
        if (!this.empty || this.tableGroupId != null) {
            throw new IllegalStateException();
        }
    }

    public void ungroup() {
        validateOrderCompleted();
        this.tableGroupId = null;
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
