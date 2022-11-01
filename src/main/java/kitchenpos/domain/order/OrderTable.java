package kitchenpos.domain.order;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "orderTable", fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);

        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateTableEmpty();

        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTableEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroup != null) {
            throw new IllegalArgumentException();
        }

        if (hasNotCompletedOrder()) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public boolean hasNotCompletedOrder() {
        return orders.stream()
                .anyMatch(Order::isCompletion);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeTableGroup(final TableGroup tableGroup) {
        if (tableGroup != null) {
            empty = false;
        }

        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
