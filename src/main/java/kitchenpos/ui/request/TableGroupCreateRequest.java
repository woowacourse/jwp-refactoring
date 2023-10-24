package kitchenpos.ui.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableId> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableId> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<OrderTableId> getOrderTableIds() {
        return orderTableIds;
    }

    public static class OrderTableId {

        public Long id;

        public OrderTableId() {
        }

        public OrderTableId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
