package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @NotNull
    private int numberOfGuests;

    @NotNull
    private boolean empty;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders = new ArrayList<>(); // TODO: 뭔가 이상. 나중에 등록됨

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty,
                      final List<Order> orders) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
    }

    public void add(final Order order) {
        orders.add(order);
    }

    public void updateEmpty(final boolean empty) {
        validateGrouped();
        validateOrderStatus();
        this.empty = empty;
    }

    public void ungroup() {
        validateUngroup();
        tableGroup = null;
        empty = false;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatus() {
        if (orders.isEmpty()) {
            return;
        }

        final boolean containsNotCompleteOrder = orders.stream()
                .anyMatch(it -> !it.isComplete());
        if (containsNotCompleteOrder) {
            throw new IllegalArgumentException();
        }
    }

    private void validateGrouped() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    private void validateUngroup() {
        if (orders.isEmpty()) {
            return;
        }
        final boolean notCompletedOrder = orders.stream()
                .anyMatch(it -> !it.isComplete());
        if (notCompletedOrder) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
