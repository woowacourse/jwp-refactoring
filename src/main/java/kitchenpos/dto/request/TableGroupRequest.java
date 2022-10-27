package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<OrderTableId> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTables = orderTableIds.stream()
                .map(OrderTableId::new)
                .collect(Collectors.toList());
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableId {

        private Long id;

        private OrderTableId() {
        }

        public OrderTableId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
