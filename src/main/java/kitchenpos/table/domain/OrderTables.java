package kitchenpos.table.domain;

import java.util.List;
import kitchenpos.table.exception.TableGroupException;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException("주문 테이블의 수가 유효하지 않습니다.");
        }
        this.orderTables = orderTables;
    }

    public void assignTableGroup(Long tableGroupId) {
        orderTables.forEach(orderTable -> orderTable.assignTableGroup(tableGroupId));
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
