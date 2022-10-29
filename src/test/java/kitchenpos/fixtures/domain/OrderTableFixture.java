package kitchenpos.fixtures.domain;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;

public class OrderTableFixture {

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        return createOrderTable(null, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.changeNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }

    public static class OrderTableRequestBuilder {

        private int numberOfGuests = 0;
        private boolean empty = false;

        public OrderTableRequestBuilder numberOfGuests(final int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableRequestBuilder empty() {
            this.empty = true;
            return this;
        }

        public OrderTableRequest build() {
            return new OrderTableRequest(numberOfGuests, empty);
        }
    }
}
