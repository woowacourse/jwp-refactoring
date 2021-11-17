package kitchenpos.domain;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(OrderTableBuilder orderTableBuilder) {
        this.id = orderTableBuilder.id;
        this.tableGroupId = orderTableBuilder.tableGroupId;
        this.numberOfGuests = orderTableBuilder.numberOfGuests;
        this.empty = orderTableBuilder.empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public static class OrderTableBuilder {

        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public OrderTableBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OrderTableBuilder setTableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public OrderTableBuilder setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableBuilder setEmpty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(this);
        }
    }
}
