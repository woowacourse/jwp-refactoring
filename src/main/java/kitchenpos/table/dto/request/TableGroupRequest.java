package kitchenpos.table.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<OrderTableId> orderTableIds;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds.stream()
                .map(OrderTableId::new)
                .collect(Collectors.toList());
    }

    public List<OrderTableId> getOrderTableIds() {
        return orderTableIds;
    }

    public List<Long> getParsedOrderTableIds() {
        return orderTableIds.stream()
                .map(OrderTableId::getId)
                .collect(Collectors.toList());
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
