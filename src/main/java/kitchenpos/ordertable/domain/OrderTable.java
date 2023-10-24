package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.ordertable.exception.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public boolean isExistTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void unGroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeEmptyStatus(final boolean isEmpty) {
        this.empty = isEmpty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateAvailableChangeNumberOfGuests();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateAvailableChangeNumberOfGuests() {
        if (this.empty == true) {
            throw new OrderTableException.CannotChangeNumberOfGuestsStateInEmptyException();
        }
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
