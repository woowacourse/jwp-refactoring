package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_CHANGE_NUMBER_OF_GUEST;

import java.util.Objects;
import kitchenpos.application.exception.CustomIllegalArgumentException;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new CustomIllegalArgumentException(INVALID_CHANGE_NUMBER_OF_GUEST);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }
    public void clearTableGroup() {
        this.tableGroupId = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }


    public void clearTable() {
        this.empty = true;
    }
}
