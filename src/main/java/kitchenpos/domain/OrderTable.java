package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, TableGroup tableGroup, List<Order> orders, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, new ArrayList<>(), numberOfGuests, empty);
    }

    public OrderTable() {
    }

    public void belongsTo(TableGroup tableGroup) {
        if (!isEmpty()) {
            throw new IllegalStateException("빈 테이블이 아닙니다.");
        }
        if (this.tableGroup != null) {
            throw new IllegalStateException("이미 단체 지정된 테이블입니다.");
        }
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        if (this.tableGroup == null) {
            throw new IllegalStateException("아직 단체 지정되지 않은 테이블입니다.");
        }
        orders.forEach(Order::validateNotCompleted);
        this.tableGroup = null;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroup);
    }

    public void changeEmpty(boolean empty) {
        if (this.tableGroup != null) {
            throw new IllegalStateException("이미 단체 지정된 테이블입니다.");
        }
        orders.forEach(Order::validateNotCompleted);
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("고객 수는 양수여야 합니다.");
        }
        validateNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNotEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException("빈 테이블입니다.");
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
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
}
