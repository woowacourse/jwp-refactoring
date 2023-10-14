package kitchenpos.domain;

import static kitchenpos.domain.exception.OrderTableExceptionType.NUMBER_OF_GUEST_LOWER_THAN_ZERO;
import static kitchenpos.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP;

import kitchenpos.domain.exception.OrderTableException;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(
        final Long id,
        final Long tableGroupId,
        final int numberOfGuests,
        final boolean empty
    ) {
        validateNumberOfGuest(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    private void validateNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException(NUMBER_OF_GUEST_LOWER_THAN_ZERO);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    //TODO : 추후 제거
    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroupId != null) {
            throw new OrderTableException(TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP);
        }
        this.empty = empty;
    }
}
