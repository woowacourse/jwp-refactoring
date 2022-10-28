package kitchenpos.table.domain;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

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

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void occupyTableGroup(Long tableGroupId) {
        shouldNotJoinTableGroup();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void unOccupyTableGroup() {
        this.tableGroupId = null;
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
}
