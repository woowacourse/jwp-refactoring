package kitchenpos.domain.table;

import kitchenpos.exception.orderTableException.IllegalOrderTableGuestNumberException;

import javax.persistence.Column;
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

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validateGuestNumber(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
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

    public void changeTableGroup(final Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void ungroup(){
        empty = true;
        tableGroupId = null;
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
}
