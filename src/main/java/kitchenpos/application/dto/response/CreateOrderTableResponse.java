package kitchenpos.application.dto.response;

import kitchenpos.domain.OrderTable;

public class CreateOrderTableResponse {
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private CreateOrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }


    public static CreateOrderTableResponse from(final OrderTable orderTable) {
        return builder()
                .id(orderTable.getId())
                .numberOfGuests(orderTable.getNumberOfGuests())
                .empty(orderTable.isEmpty())
                .build();
    }

    public static CreateOrderTableResponseBuilder builder() {
        return new CreateOrderTableResponseBuilder();
    }

    public static final class CreateOrderTableResponseBuilder {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        private CreateOrderTableResponseBuilder() {
        }

        public CreateOrderTableResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateOrderTableResponseBuilder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public CreateOrderTableResponseBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public CreateOrderTableResponseBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public CreateOrderTableResponse build() {
            return new CreateOrderTableResponse(id, tableGroupId, numberOfGuests, empty);
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
