package kitchenpos.application.dto.response;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return builder()
                .id(orderTable.getId())
                .numberOfGuests(orderTable.getNumberOfGuests())
                .empty(orderTable.isEmpty())
                .build();
    }

    public static OrderTableResponseBuilder builder() {
        return new OrderTableResponseBuilder();
    }

    public static final class OrderTableResponseBuilder {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        private OrderTableResponseBuilder() {
        }

        public OrderTableResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderTableResponseBuilder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public OrderTableResponseBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableResponseBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTableResponse build() {
            return new OrderTableResponse(id, tableGroupId, numberOfGuests, empty);
        }
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
}
