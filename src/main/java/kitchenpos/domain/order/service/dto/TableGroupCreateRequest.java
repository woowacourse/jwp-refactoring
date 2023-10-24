package kitchenpos.domain.order.service.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<Long> orderTables;

    public TableGroupCreateRequest(final List<Long> orderTables) {
        validateNotNull(orderTables);
        this.orderTables = orderTables;
    }

    private void validateNotNull(final List<Long> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("TableGroupCreateReqeuest의 입력값이 비어선 안됩니다.");
        }
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
