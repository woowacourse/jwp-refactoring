package kitchenpos.table.application.dto;

public class OrderTableRequest {
    private Long id;

    private OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static class Create {

        private int numberOfGuests;
        private boolean empty;

        public Create(int numberOfGuests, boolean empty) {
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        private Create() {
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public boolean isEmpty() {
            return empty;
        }
    }

    public static class Empty {
        private boolean empty;

        private Empty() {
        }

        public Empty(boolean empty) {
            this.empty = empty;
        }

        public boolean isEmpty() {
            return empty;
        }
    }

    public static class NumberOfGuest {
        private int numberOfGuests;

        private NumberOfGuest() {
        }

        public NumberOfGuest(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }
    }
}
