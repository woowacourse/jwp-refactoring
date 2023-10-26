package kitchenpos.dto.request;

public class CreateOrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    private CreateOrderTableRequest() {
    }

    private CreateOrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static CreateOrderTableRequestBuilder builder() {
        return new CreateOrderTableRequestBuilder();
    }

    public static final class CreateOrderTableRequestBuilder {

        private int numberOfGuests;
        private boolean empty;

        private CreateOrderTableRequestBuilder() {
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
            return new CreateOrderTableRequest(numberOfGuests, empty);
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
