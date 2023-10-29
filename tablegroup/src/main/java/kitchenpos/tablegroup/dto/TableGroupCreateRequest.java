package kitchenpos.tablegroup.dto;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    public void validateOrderTables(final List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블은 빈 값이거나 빈 리스트 일 수 없습니다.");
        }
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
