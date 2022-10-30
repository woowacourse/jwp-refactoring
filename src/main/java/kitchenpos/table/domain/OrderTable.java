package kitchenpos.table.domain;

import java.util.Objects;
import kitchenpos.table.exception.InvalidNumberOfGuestsException;
import kitchenpos.table.exception.InvalidOrderCreateTableEmpty;
import kitchenpos.table.exception.InvalidTableGroupJoinException;

public class OrderTable {

    private final Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public void occupyTableGroup(Long tableGroupId) {
        shouldNotJoinTableGroup();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    private void shouldNotJoinTableGroup() {
        if (!canJoinWithTableGroupStatus()) {
            throw new InvalidTableGroupJoinException();
        }
    }

    private boolean canJoinWithTableGroupStatus() {
        return (this.empty && Objects.isNull(this.tableGroupId));
    }

    public void unOccupyTableGroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeEmpty(Boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0 || empty) {
            throw new InvalidNumberOfGuestsException();
        }
    }

    public void validateNotEmpty() {
        if (empty) {
            throw new InvalidOrderCreateTableEmpty();
        }
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
