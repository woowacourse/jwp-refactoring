package kitchenpos.application.dto.request;

public class CreateOrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private CreateOrderTableRequest() {
    }

    public static CreateOrderTableRequestBuilder builder() {
        return new CreateOrderTableRequestBuilder();
    }

    public static final class CreateOrderTableRequestBuilder {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        private CreateOrderTableRequestBuilder() {
        }

        public CreateOrderTableRequestBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateOrderTableRequestBuilder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public CreateOrderTableRequestBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public CreateOrderTableRequestBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public CreateOrderTableRequest build() {
            CreateOrderTableRequest createOrderTableRequest = new CreateOrderTableRequest();
            createOrderTableRequest.tableGroupId = this.tableGroupId;
            createOrderTableRequest.numberOfGuests = this.numberOfGuests;
            createOrderTableRequest.empty = this.empty;
            createOrderTableRequest.id = this.id;
            return createOrderTableRequest;
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
