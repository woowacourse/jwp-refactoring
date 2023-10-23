package kitchenpos.application.support.domain;

import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;

public class OrderTableTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static Long autoCount = 0L;

        private Long id = ++autoCount;
        private Long tableGroupId = null;
        private int numberOfGuests = 4;
        private boolean empty = false;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder tableGroupId(final Long tableGroupId) {
            this.tableGroupId = tableGroupId;
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
            final var result = new OrderTable();
            result.setId(id);
            result.setTableGroupId(tableGroupId);
            result.setNumberOfGuests(numberOfGuests);
            result.setEmpty(empty);
            return result;
        }

        public OrderTableCreateRequest buildToOrderTableCreateRequest() {
            return new OrderTableCreateRequest(numberOfGuests, empty);
        }
    }
}
