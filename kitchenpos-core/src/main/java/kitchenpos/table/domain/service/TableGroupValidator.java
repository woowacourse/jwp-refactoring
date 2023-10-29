package kitchenpos.table.domain.service;

import java.util.List;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupValidator {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    public void validate(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();
        validateOrderTables(orderTables);
        validateOrderTableSize(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        orderTables.stream()
            .filter(orderTable -> !orderTable.isEmpty() || orderTable.getTableGroupId() != null)
            .findAny()
            .ifPresent(orderTable -> {
                throw new IllegalArgumentException("올바르지 않은 주문 테이블입니다.");
            });
    }

    private void validateOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }
    }
}
