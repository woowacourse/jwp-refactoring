package kitchenpos.domain;

import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void validateTableStatus() {
        for (OrderTable orderTable : this.orderTables) {
            if (!orderTable.isEmpty() || orderTable.getTableGroupId() != null) {
                throw new IllegalArgumentException("비어 있지 않거나, 이미 그룹으로 지정된 테이블은 단체로 지정할 수 없습니다.");
            }
        }
    }

    public void group(Long id) {
        for (OrderTable orderTable : this.orderTables) {
            orderTable.group(id);
        }
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
