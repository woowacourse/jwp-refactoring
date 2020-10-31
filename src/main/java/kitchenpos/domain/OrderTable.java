package kitchenpos.domain;

import kitchenpos.domain.exceptions.InvalidForChangingNumberOfGuestsException;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() { }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public void changeEmptyState(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0 || empty) {
            throw new InvalidForChangingNumberOfGuestsException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        this.tableGroup = null;
        changeEmptyState(false);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
