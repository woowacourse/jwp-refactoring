package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    @OneToMany(mappedBy = "orderTableId")
    private List<Order> orders;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty, List.of());
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty,
                      final List<Order> orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new ArrayList<>(orders);
    }

    public void changeEmptyStatus(final boolean isEmpty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }

        for (Order order : orders) {
            if (order.getOrderStatus() != COMPLETION) {
                throw new IllegalArgumentException();
            }
        }

        this.empty = isEmpty;
    }

    public void changeNumberOfGuest(final int numberOfGuest) {
        if (numberOfGuest < 0 || empty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuest;
    }

    void joinGroup(final Long newGroupId) {
        if (newGroupId == null || !empty || tableGroupId != null) {
            throw new IllegalArgumentException();
        }

        tableGroupId = newGroupId;
        empty = false;
    }

    public void leaveGroup() {
        for (Order order : orders) {
            if (order.getOrderStatus() != COMPLETION) {
                throw new IllegalArgumentException();
            }
        }

        tableGroupId = null;
        empty = false;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", tableGroupId=" + tableGroupId +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                ", orders=" + orders +
                '}';
    }
}
