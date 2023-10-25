package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(mappedBy = "orderTable", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private final List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    private OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private OrderTable(
            Long id,
            TableGroup tableGroup,
            NumberOfGuests numberOfGuests,
            boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(
            int numberOfGuests,
            boolean empty
    ) {
        return new OrderTable(
                NumberOfGuests.from(numberOfGuests),
                empty
        );
    }

    public void addOrder(Order orders) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.orders.add(orders);
        orders.registerOrderTable(this);
    }

    public void registerTableGroup(TableGroup tableGroup) {
        if (!isEmpty() || isAlreadyContainsTableGroup()) {
            throw new IllegalArgumentException();
        }

        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void breakupTableGroup() {
        boolean canNotBreakup = orders.stream()
                .anyMatch(order -> order.isCooking() || order.isMeal());

        if (canNotBreakup) {
            throw new IllegalArgumentException();
        }

        empty = false;
        tableGroup = null;
    }

    public boolean isAlreadyContainsTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void changeEmpty(boolean empty) {
        if (isAlreadyContainsTableGroup()) {
            throw new IllegalArgumentException();
        }

        boolean canNotChangeEmpty = orders.stream()
                .anyMatch(order -> order.isCooking() || order.isMeal());

        if (canNotChangeEmpty) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    public List<Order> getOrders() {
        return orders;
    }

}
