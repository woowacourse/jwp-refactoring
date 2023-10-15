package kitchenpos.domain.ordertable;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private NumberOfGuests count;
    private Emptiness empty;

    public OrderTable(final NumberOfGuests count, final Emptiness empty) {
        this(null, null, count, empty);
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests count, Emptiness empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.count = count;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getCount() {
        return count.getValue();
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public boolean isEmpty() {
        return empty.getValue();
    }

    public void updateTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateNumberOfGuests(final NumberOfGuests count) {
        this.count = count;
    }

    public void updateEmpty(final Emptiness empty) {
        this.empty = empty;
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
