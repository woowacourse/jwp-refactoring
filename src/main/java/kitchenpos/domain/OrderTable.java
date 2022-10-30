package kitchenpos.domain;

import java.util.Objects;
import kitchenpos.exception.GuestSizeException;
import kitchenpos.exception.TableGroupNotNullException;
import kitchenpos.exception.UnableToGroupingException;

public class OrderTable {
    private final Long id;
    private final Long tableGroupId;
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
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(OrderTable orderTable, Long tableGroupId) {
        this(orderTable.id, tableGroupId, orderTable.numberOfGuests, orderTable.empty);
    }

    public void validateAbleToGrouping() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new UnableToGroupingException();
        }
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new TableGroupNotNullException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0 || isEmpty()) {
            throw new GuestSizeException();
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
}
