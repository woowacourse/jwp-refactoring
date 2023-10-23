package kitchenpos.domain;

import kitchenpos.exception.orderTableException.IllegalOrderTableGuestNumberException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validateGuestNumber(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmptyStatus(final boolean empty) {
        this.empty = empty;
    }

    private void validateGuestNumber(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalOrderTableGuestNumberException();
        }
    }

    public void changeTableGroup(final TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void ungroup(){
        empty = true;
        tableGroup = null;
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
