package kitchenpos.domain;

import kitchenpos.exception.table.CannotChangeTableGroupAsAlreadyAssignedException;
import kitchenpos.exception.table.CannotChangeTableGroupAsNotEmpty;
import kitchenpos.exception.table.CannotChangeTableStatusAsAlreadyAssignedTableGroupException;
import kitchenpos.exception.table.InvalidNumberOfGuestsException;

import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);

        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;

        if (tableGroup != null) {
            tableGroup.getOrderTables().add(this);
        }
    }

    public void joinToTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != null) {
            throw new CannotChangeTableGroupAsAlreadyAssignedException();
        }
        if (!isEmpty()) {
            throw new CannotChangeTableGroupAsNotEmpty();
        }
        if (tableGroup != null) {
            tableGroup.getOrderTables().add(this);
        }
        changeStatus(false);
        this.tableGroup = tableGroup;
    }

    public void changeStatus(boolean isEmpty) {
        if (this.tableGroup != null) {
            throw new CannotChangeTableStatusAsAlreadyAssignedTableGroupException();
        }
        this.empty = isEmpty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestsException();
        }
    }

    public void ungroup() {
        if (this.tableGroup != null) {
            this.tableGroup.getOrderTables().remove(this);
        }
        this.tableGroup = null;
        changeStatus(false);
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
