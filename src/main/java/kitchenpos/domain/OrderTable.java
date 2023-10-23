package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, List<Order> orders, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, List<Order> orders, int numberOfGuests) {
        this.tableGroup = tableGroup;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
    }

    public void updateEmpty(boolean isEmpty) {
        validateTableGroupIsNotNull();
        this.empty = isEmpty;
    }

    public Long getId() {
        return id;
    }

    public List<Order> getOrders() {
        return orders;
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

    public void validateTableGroupIsNotNull() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("TableGroup이 Null일 수 없습니다.");
        }
    }

    public void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수가 음수일 수 없습니다.");
        }
    }

    public void validateIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("OrderTable이 비어있지 않아야 합니다.");
        }
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

}
