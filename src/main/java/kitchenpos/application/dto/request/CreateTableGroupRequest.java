package kitchenpos.application.dto.request;

import java.util.List;

public class CreateTableGroupRequest {
    private List<CreateOrderTable> orderTables;

    private CreateTableGroupRequest() {
    }

    private CreateTableGroupRequest(List<CreateOrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static class CreateOrderTable {
        private long id;

        private CreateOrderTable() {
        }

        public CreateOrderTable(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    public static CreateTableGroupRequestBuilder builder() {
        return new CreateTableGroupRequestBuilder();
    }

    public static final class CreateTableGroupRequestBuilder {
        private List<CreateOrderTable> orderTables;

        private CreateTableGroupRequestBuilder() {
        }

        public CreateTableGroupRequestBuilder orderTables(List<CreateOrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public CreateTableGroupRequest build() {
            return new CreateTableGroupRequest(orderTables);
        }
    }

    public List<CreateOrderTable> getOrderTables() {
        return orderTables;
    }
}
