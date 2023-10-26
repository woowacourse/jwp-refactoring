package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Order order(List<OrderLineItem> items) {
        if (empty) {
            throw new IllegalArgumentException("해당 테이블은 비어있습니다.");
        }
        return new Order(null, this.id, OrderStatus.COOKING.name(), LocalDateTime.now(), items);
    }

    public void ungroup() {
        tableGroup = null;
        empty = false;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroup != null) {
            throw new IllegalArgumentException("해당 주문 테이블은 그룹화 되어있기 때문에 빈 여부를 변경할 수 없습니다");
        }
        this.empty = empty;
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

    public void grouping(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void clearing() {
        this.empty = true;
    }

    public void filling() {
        this.empty = false;
    }

    public boolean isEmpty() {
        return empty;
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
}
