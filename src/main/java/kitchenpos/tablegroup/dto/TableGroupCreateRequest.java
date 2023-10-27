package kitchenpos.tablegroup.dto;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블을 입력해주세요");
        }
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableRequest {

        private Long id;

        private OrderTableRequest() {
        }

        public OrderTableRequest(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
