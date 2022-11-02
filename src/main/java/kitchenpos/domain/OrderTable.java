package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
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

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final NumberOfGuests numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasTableGroup() {
        return this.tableGroupId != null;
    }

    public void ungroup() {
        removeTableGroup();
        updateEmptyStatus(false);
    }

    private void removeTableGroup() {
        this.tableGroupId = null;
    }

    public void updateEmptyStatus(final boolean empty) {
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void updateNumberOfGuests(final NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }
}
