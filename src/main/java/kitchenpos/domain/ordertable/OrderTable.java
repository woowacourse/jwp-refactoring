package kitchenpos.domain.ordertable;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private NumberOfGuests numberOfGuests;
    private Empty empty;

    public OrderTable(final NumberOfGuests numberOfGuests, final Empty empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final NumberOfGuests numberOfGuests,
                      final Empty empty) {
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
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty.getValue();
    }

    public void updateTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateNumberOfGuests(final NumberOfGuests count) {
        this.numberOfGuests = count;
    }

    public void updateEmpty(final Empty empty) {
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
