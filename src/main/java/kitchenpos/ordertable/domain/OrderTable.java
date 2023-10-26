package kitchenpos.ordertable.domain;

import java.util.Objects;

public class OrderTable {

    public static final boolean EMPTY = true;
    public static final boolean NOT_EMPTY = false;

    private Long id;
    private Long tableGroupId;
    private OrderTableGuestCount numberOfGuests;
    private boolean empty;

    private OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new OrderTableGuestCount(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public static Builder builder(final int numberOfGuests, final boolean empty) {
        return new Builder(numberOfGuests, empty);
    }

    public boolean hasTableGroupId() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = new OrderTableGuestCount(numberOfGuests);
    }

    public void belongsToTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public boolean hasNoGroupAndEmpty() {
        return Objects.isNull(tableGroupId) && empty;
    }

    public void unGroup() {
        this.tableGroupId = null;
        this.empty = OrderTable.NOT_EMPTY;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getCount();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTable orderTable = (OrderTable) o;
        if (Objects.isNull(this.id) || Objects.isNull(orderTable.id)) {
            return false;
        }
        return Objects.equals(id, orderTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {

        private Long id;
        private Long tableGroupId;
        private final int numberOfGuests;
        private final boolean empty;

        public Builder(final int numberOfGuests, final boolean empty) {
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder tableGroupId(final Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(id, tableGroupId, numberOfGuests, empty);
        }
    }
}
