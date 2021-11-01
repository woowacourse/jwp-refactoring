package kitchenpos.common.fixture;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;

public class TOrderTable {

    public static Builder builder() {
        return new Builder();
    }

    public static MultiBuilder multiBuilder() {
        return new MultiBuilder();
    }

    public static class Builder {

        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public Builder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public Builder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            OrderTable orderTable = new OrderTable();
            orderTable.setId(id);
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setNumberOfGuests(numberOfGuests);
            orderTable.setEmpty(empty);

            return orderTable;
        }
    }

    public static class MultiBuilder {

        private List<OrderTable> orderTables;

        public MultiBuilder() {
            this.orderTables = new ArrayList<>();
        }

        public MultiBuilder create(int numberOfGuest, boolean isEmpty) {
            OrderTable orderTable = generate(numberOfGuest, isEmpty);
            this.orderTables.add(orderTable);
            return this;
        }

        public MultiBuilder createDefault(int repeat) {
            for (int i = 0; i < repeat; i++) {
                OrderTable orderTable = generate(0, true);
                this.orderTables.add(orderTable);
            }
            return this;
        }

        public MultiBuilder createDefault() {
            OrderTable orderTable = generate(0, true);
            this.orderTables.add(orderTable);
            return this;
        }

        private OrderTable generate(int numberOfGuest, boolean isEmpty) {
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(numberOfGuest);
            orderTable.setEmpty(isEmpty);
            return orderTable;
        }

        public List<OrderTable> build() {
            return orderTables;
        }
    }
}
