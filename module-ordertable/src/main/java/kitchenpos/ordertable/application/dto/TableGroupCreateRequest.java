package kitchenpos.ordertable.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<OrderTableId> orderTables;

    public TableGroupCreateRequest(final List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables
                .stream()
                .map(OrderTableId::getId)
                .collect(Collectors.toList());
    }

    public static class OrderTableId {

        private final Long id;

        public OrderTableId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
