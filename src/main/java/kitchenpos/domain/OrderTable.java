package kitchenpos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class OrderTable {

    @Id
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    @PersistenceCreator
    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public OrderTable changeNumberOfGuests(final int numberOfGuests) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable changeEmpty(final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
