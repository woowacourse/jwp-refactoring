package kitchenpos.domain;

import kitchenpos.domain.vo.NumberOfGuests;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column
    private boolean empty;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
    private TableGroup tableGroup;

    protected OrderTable() {
    }

    private OrderTable(final Long id, final NumberOfGuests numberOfGuests, final boolean empty, final TableGroup tableGroup) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public OrderTable(final NumberOfGuests numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty, null);
    }

    public void ungroup() {
        empty = false;
        tableGroup = null;
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        validateOccupied();
        this.numberOfGuests = numberOfGuests;
    }

    public void validateOccupied() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void changeToOccupied() {
        empty = false;
    }

    public boolean canGroup() {
        return tableGroup == null && empty;
    }

    public void changeEmpty(final boolean empty) {
        validateTableGroupNull();
        this.empty = empty;
    }

    private void validateTableGroupNull() {
        if (tableGroup != null) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
