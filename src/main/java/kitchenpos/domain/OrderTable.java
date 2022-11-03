package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_group_id")
    private Long orderTableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isTableGrouped() {
        return orderTableGroupId != null;
    }

    public void ungroup() {
        orderTableGroupId = null;
        empty = false;
    }

    public void group(Long orderTableGroupId) {
        this.orderTableGroupId = orderTableGroupId;
    }

    public void changeEmpty() {
        empty = true;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableGroupId() {
        return orderTableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
