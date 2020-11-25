package kitchenpos.domain;

import java.util.Objects;

import kitchenpos.exception.NotEnoughGuestsException;
import kitchenpos.exception.TableEmptyException;
import kitchenpos.exception.TableGroupExistenceException;

public class Table {
    public static final int MIN_NUMBER_OF_GUESTS = 0;

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public Table() {
        this.id = null;
        this.tableGroupId = null;
        this.numberOfGuests = 0;
        this.empty = true;
    }

    public Table(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void joinIn(long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = true;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateChangeable(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateChangeable(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new NotEnoughGuestsException();
        }
        if (isEmpty()) {
            throw new TableEmptyException();
        }
    }

    public void changeEmpty(final boolean empty) {
        if (this.hasGroup()) {
            throw new TableGroupExistenceException();
        }
        this.empty = empty;
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroupId);
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
