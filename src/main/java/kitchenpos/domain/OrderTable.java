package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final NumberOfGuests numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasTableGroup() {
        return tableGroup != null && tableGroup.getId() != null;
    }

    public Long getId() {
        return id;
    }

    public void removeTableGroup() {
        this.tableGroup = null;
    }

    public void updateNumberOfGuests(final NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public void updateEmptyStatus(final boolean empty) {
        this.empty = empty;
    }
}
