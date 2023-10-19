package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.CannotChangeNumberOfGuestBecauseOfEmptyTableException;
import kitchenpos.ordertable.exception.NumberOfGuestsInvalidException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.TableGroupAlreadyRegisteredInGroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ORDER_TABLE")
public class OrderTable {

    private static final int ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);

        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;

        belongToTableGroup(tableGroup);
    }

    private void belongToTableGroup(final TableGroup tableGroup) {
        if (tableGroup != null) {
            tableGroup.getOrderTables()
                    .add(this);
        }
    }

    public void updateEmptyStatus(final boolean status) {
        if (this.tableGroup != null) {
            throw new TableGroupAlreadyRegisteredInGroup();
        }

        this.empty = status;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateEmptyTable();

        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < ZERO) {
            throw new NumberOfGuestsInvalidException();
        }
    }

    private void validateEmptyTable() {
        if (this.isEmpty()) {
            throw new CannotChangeNumberOfGuestBecauseOfEmptyTableException();
        }
    }

    public void initTableGroup(final TableGroup tableGroup) {
        if (this.tableGroup != null) {
            throw new TableGroupAlreadyRegisteredInGroup();
        }

        if (!isEmpty()) {
            throw new IllegalArgumentException();
        }

        belongToTableGroup(tableGroup);

        changeStatus(false);
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        if (this.tableGroup != null) {
            this.tableGroup.getOrderTables()
                    .remove(this);
        }

        this.tableGroup = null;
        changeStatus(false);
    }

    private void changeStatus(final boolean isEmpty) {
        if (this.tableGroup != null) {
            throw new CannotChangeNumberOfGuestBecauseOfEmptyTableException();
        }

        this.empty = isEmpty;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTable)) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
