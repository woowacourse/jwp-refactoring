package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_CHANGE_NUMBER_OF_GUEST;
import static kitchenpos.application.exception.ExceptionType.INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION;

import java.util.Objects;
import kitchenpos.application.exception.CustomIllegalArgumentException;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    // OrderStatus 넣을까 말까 ..

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

    public void validTableGroupCondition() {
        if (Objects.nonNull(tableGroupId)) {
            throw new CustomIllegalArgumentException(INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION);
        }
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void clearTable() {
        validTableGroupCondition();
        this.empty = true;
    }

    private void clearTableGroup() {
        this.tableGroupId = null;
    }

    public void clear() {
        clearTableGroup();
        clearTable();
    }
}
