package kitchenpos.ui.request;

import org.springframework.util.CollectionUtils;

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

    public void validate() {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        long count = orderTableIds.stream()
                .distinct()
                .count();

        if (count != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
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
