package kitchenpos.domain.order_table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    @Column(name = "number_of_guests")
    private int numberOfGuests;
    @Column(name = "empty", columnDefinition = "BIT")
    private boolean empty;


    public OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    public void updateTableGroupId(Long tableGroupId) {
        if (hasTableGroup()) {
            throw new IllegalArgumentException();
        }
        this.tableGroupId = tableGroupId;
    }

    public void updateEmpty(Boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void removeTableGroup() {
        this.tableGroupId = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }


    public boolean isEmpty() {
        return empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }
}
