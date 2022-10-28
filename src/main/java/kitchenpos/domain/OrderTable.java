package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
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
}
