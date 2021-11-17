package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    protected TableGroupCreateRequest() {
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> orderTableIds() {
        return getOrderTables().stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public static class OrderTableRequest {

        private long id;

        protected OrderTableRequest() {
        }

        public long getId() {
            return id;
        }
    }
}
