package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.CannotChangeNumberOfGuestBecauseOfEmptyTableException;
import kitchenpos.ordertable.exception.NumberOfGuestsInvalidException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(nullable = false, name = "number_of_guests")
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateTableGroupStatus(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateEmptyStatus(final boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if(numberOfGuests <= 0) {
            throw new NumberOfGuestsInvalidException();
        }

        if(empty) {
            throw new CannotChangeNumberOfGuestBecauseOfEmptyTableException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
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
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
