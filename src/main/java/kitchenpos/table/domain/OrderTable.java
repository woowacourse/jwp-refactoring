package kitchenpos.table.domain;

import static kitchenpos.table.domain.TableStatus.EMPTY;

import java.util.Objects;
import org.springframework.data.annotation.Id;

public class OrderTable {

    @Id
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;
    private final TableStatus tableStatus;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty,
                      final TableStatus tableStatus) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableStatus = tableStatus;
    }

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty, EMPTY);
    }

    public OrderTable group(final Long tableGroupId) {
        return new OrderTable(id, tableGroupId, numberOfGuests, false, tableStatus);
    }

    public OrderTable changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, tableStatus);
    }

    public OrderTable changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
        if (tableStatus.isEatIn()) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, tableStatus);
    }

    public void verifyCanGroup() {
        final boolean canGroup = empty && tableGroupId == null;
        if (!canGroup) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable ungroup() {
        if (tableStatus.isEatIn()) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, null, numberOfGuests, false, tableStatus);
    }

    public OrderTable changeTableStatus(final TableStatus tableStatus) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, tableStatus);
    }

    public boolean isEatIn() {
        return tableStatus.isEatIn();
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

    public TableStatus getTableStatus() {
        return tableStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
