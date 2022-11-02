package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @OneToOne(mappedBy="orderTable")
    private Order order;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {}

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public void empty() {
        this.empty = true;
    }

    public void full() {
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void enrollTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void enrollOrder(Order order) {
        this.order = order;
    }

    public boolean hasTableGroup() {
        return this.tableGroup != null;
    }

    public boolean isCookingOrMeal() {
        return getOrder().isCooking() || getOrder().isMeal();
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

    public Order getOrder() {
        return order;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void registerTableGroup(final TableGroup tableGroup) {
        this.full();
        this.tableGroup = tableGroup;
    }

    public void unregisterTableGroup() {
        this.empty();
        this.tableGroup = null;
    }
}
