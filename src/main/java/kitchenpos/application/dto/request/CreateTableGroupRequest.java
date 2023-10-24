package kitchenpos.application.dto.request;

import java.util.List;

public class CreateTableGroupRequest {
    private List<CreateOrderTable> orderTables;

    private CreateTableGroupRequest() {
    }

    public static class CreateOrderTable {
        private final long id;

        public CreateOrderTable(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    public List<CreateOrderTable> getOrderTables() {
        return orderTables;
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
            CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest();
            createTableGroupRequest.orderTables = this.orderTables;
            return createTableGroupRequest;
        }
    }
}
