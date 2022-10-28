package kitchenpos.domain;

import java.util.Objects;
import org.springframework.data.annotation.Id;

public class OrderTable {

    @Id
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

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
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable ungroup(final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
