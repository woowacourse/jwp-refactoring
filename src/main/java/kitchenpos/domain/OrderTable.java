package kitchenpos.domain;

import java.util.Objects;
import kitchenpos.exception.AlreadyGroupedException;
import kitchenpos.exception.CanNotGroupException;
import kitchenpos.exception.NumberOfGuestsSizeException;
import kitchenpos.exception.TableEmptyException;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void validateOrderable() {
        if (empty) {
            throw new TableEmptyException();
        }
    }

    public void updateEmpty(final boolean empty) {
        validateNotGrouping();
        this.empty = empty;
    }

    private void validateNotGrouping() {
        if (Objects.nonNull(tableGroupId)) {
            throw new AlreadyGroupedException();
        }
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateOrderable();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NumberOfGuestsSizeException();
        }
    }

    public void groupTableBy(final Long tableGroupId) {
        validateGroupable();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    private void validateGroupable() {
        if (!empty | Objects.nonNull(tableGroupId)) {
            throw new CanNotGroupException();
        }
    }

    public void ungroup() {
        this.tableGroupId = null;
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
}
