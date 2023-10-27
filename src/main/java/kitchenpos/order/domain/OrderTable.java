package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.table_group.domain.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    @Embedded
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final int numberOfGuests, final boolean empty, final List<Order> orders) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
        this.orders = new Orders(orders, this);
    }

    public static OrderTable forSave(final int numberOfGuests, final boolean empty, final List<Order> orders) {
        return new OrderTable(null, numberOfGuests, empty, orders);
    }

    public void changeEmpty(final boolean empty) {
        if (empty && hasProceedingOrder()) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 빈 테이블로 설정할 수 없습니다.");
        }

        this.empty = empty;
    }

    public boolean hasProceedingOrder() {
        return orders.hasProceedingOrder();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void registerTableGroup(final TableGroup tableGroup) {
        validateEmptyStatus();
        this.tableGroup = tableGroup;
    }

    private void validateEmptyStatus() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블에는 테이블 그룹을 추가할 수 없습니다.");
        }
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders.getOrders());
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
