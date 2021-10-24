package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderTable")
    private List<Order> orders;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty, List<Order> orders) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public void toTableGroup(TableGroup tableGroup) {
        if (!this.empty || Objects.nonNull(getTableGroup())) {
            throw new IllegalArgumentException("OrderTable이 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.");
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경하려는 손님 수는 음수일 수 없습니다.");
        }
        if (this.empty) {
            throw new IllegalArgumentException("OrderTable이 비어있는 상태입니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(getTableGroup())) {
            throw new IllegalArgumentException("OrderTable이 속한 TableGroup이 존재합니다.");
        }
        validateOrderStatus();
        this.empty = empty;
    }

    public void ungroup() {
        validateOrderStatus();
        this.tableGroup = null;
        this.empty = false;
    }

    private void validateOrderStatus() {
        getOrders().stream()
            .filter(order -> order.getOrderStatus() != OrderStatus.COMPLETION)
            .findAny()
            .ifPresent(order -> {
                throw new IllegalArgumentException("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.");
            });
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
