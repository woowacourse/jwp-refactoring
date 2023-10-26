package kitchenpos.table.domain;


import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void ungroup(final OrderTablesValidator orderTablesValidator) {
        orderTablesValidator.validate(this);
        orderTables.forEach(OrderTable::ungroup);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
