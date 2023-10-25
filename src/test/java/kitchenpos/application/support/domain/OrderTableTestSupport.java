package kitchenpos.application.support.domain;

import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private TableGroup tableGroup = null;
        private int numberOfGuests = 4;
        private boolean empty = false;

        public Builder tableGroup(final TableGroup tableGroup) {
            this.tableGroup = tableGroup;
            return this;
        }

        public Builder numberOfGuests(final int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public Builder empty(final boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            final var result = new OrderTable(numberOfGuests, empty);
            result.updateTableGroup(tableGroup);
            return result;
        }

        public OrderTableCreateRequest buildToOrderTableCreateRequest() {
            return new OrderTableCreateRequest(numberOfGuests, empty);
        }
    }
}
