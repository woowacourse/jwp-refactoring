package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void group(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        changeEmpty(true);
    }

    public void unGroup() {
        this.tableGroup = null;
        changeEmpty(false);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        validateAbleToChangeEmpty();
        this.empty = empty;
    }

    private void validateAbleToChangeEmpty() {
        final boolean cannotEmptyTable = orders.stream()
                .anyMatch(Order::isInProgress);
        if (cannotEmptyTable) {
            throw new IllegalArgumentException("조리 또는 식사 중인 테이블을 비울 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void addOrder(final Order order) {
        orders.add(order);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
