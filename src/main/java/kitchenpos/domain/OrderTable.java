package kitchenpos.domain;

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
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeEmptyStatus(final boolean isEmpty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }

        for (Order order : orders) {
            if (!order.getOrderStatus().equals("COMPLETION")) {
                throw new IllegalArgumentException();
            }
        }

        this.empty = isEmpty;
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

    public void changeNumberOfGuest(final int numberOfGuest) {
        if (numberOfGuest < 0 || empty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuest;
    }

    void joinToGroup(final Long id) {
        if (id == null || !empty || tableGroupId != null) {
            throw new IllegalArgumentException();
        }

        tableGroupId = id;
        empty = false;
    }
}
