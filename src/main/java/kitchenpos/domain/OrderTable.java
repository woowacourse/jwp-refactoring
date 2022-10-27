package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void setTableGroup(TableGroup tableGroup) {
        validateIsEmpty();
        if (tableGroup == null) {
            throw new IllegalArgumentException();
        }
        this.tableGroup = tableGroup;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        validateIsEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void setEmpty(final boolean empty) {
        if (tableGroup != null) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
