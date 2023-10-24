package kitchenpos.domain.table;

import org.springframework.data.annotation.Id;

public class OrderTable {
    @Id
    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        validate(numberOfGuests);
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public static OrderTableBuilder builder() {
        return new OrderTableBuilder();
    }

    public OrderTable fillTable() {
        return new OrderTable(id, numberOfGuests, false);
    }

    public OrderTable ungroup() {
        return new OrderTable(id, numberOfGuests, true);
    }

    public OrderTable updateEmpty(boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public OrderTable updateNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        return new OrderTable(id, numberOfGuests, false);
    }

    public static final class OrderTableBuilder {
        private Long id;
        private int numberOfGuests;
        private boolean empty;

        private OrderTableBuilder() {
        }

        public OrderTableBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderTableBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(id, numberOfGuests, empty);
        }
    }
}
