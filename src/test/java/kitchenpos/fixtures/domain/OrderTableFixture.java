package kitchenpos.fixtures.domain;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable(final int numberOfGuests,
                                              final boolean empty) {
        return createOrderTable(null, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(final Long tableGroupId,
                                              final int numberOfGuests,
                                              final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static class OrderTableRequestBuilder {

        private Long tableGroupId;
        private int numberOfGuests = 0;
        private boolean empty = false;

        public OrderTableRequestBuilder tableGroupId(final Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public OrderTableRequestBuilder numberOfGuests(final int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableRequestBuilder empty() {
            this.empty = true;
            return this;
        }

        public OrderTable build() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setNumberOfGuests(numberOfGuests);
            orderTable.setEmpty(empty);
            return orderTable;
        }
    }
}
