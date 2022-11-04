package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    public static final int NOT_MANY_TABLE_SIZE = 2;

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validateEmptyOrderTables(orderTables);
        validateOrderTablesSize(orderTables);
        this.orderTables = orderTables;
    }

    private void validateEmptyOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables.size() < NOT_MANY_TABLE_SIZE) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이여야 합니다.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
