package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "ORDER_TABLE_ID"))
public class OrderTable extends BaseEntity {
    private static final int MIN_NUMBER_OF_GUEST = 0;

    @ManyToOne
    @JoinColumn(name = "TABLE_GROUP_ID")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        if (numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void removeGroup() {
        updateTableGroup(null);
        updateEmpty(false);
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (isEmpty() || numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
