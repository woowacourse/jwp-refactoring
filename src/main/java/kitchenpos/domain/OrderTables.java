package kitchenpos.domain;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean isPossibleTableGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            return false;
        }
        return true;
    }

    public boolean isSameSize(int size) {
        return orderTables.size() == size;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int getOrderTablesSize() {
        return orderTables.size();
    }
}
