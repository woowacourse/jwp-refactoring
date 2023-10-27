package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public void changeEmpty(Boolean empty) {
        validateUngrouped();

        this.empty = empty;
    }

    private void validateUngrouped() {
        if (tableGroup != null) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public void changeNumberOfGuest(Integer numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        validateNotEmpty();

        this.numberOfGuests = numberOfGuests;
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void unGroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
