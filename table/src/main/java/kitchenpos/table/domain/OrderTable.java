package kitchenpos.table.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.NUMBER_OF_GUEST_LOWER_THAN_ZERO;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.table.domain.exception.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable(final int numberOfGuests, final boolean empty) {
        validateNumberOfGuest(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {

    }

    private void validateNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException(NUMBER_OF_GUEST_LOWER_THAN_ZERO);
        }
    }

    public Long getId() {
        return id;
    }

    //TODO :private으로 바꾸기
    public void changeTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuest(numberOfGuests);
        if (empty) {
            throw new OrderTableException(TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroupId != null) {
            throw new OrderTableException(TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP);
        }
        this.empty = empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void group(final Long tableGroupId) {
        changeEmpty(false);
        changeTableGroupId(tableGroupId);
    }

    public void ungroup() {
        changeTableGroupId(null);
        changeEmpty(true);
    }
}
