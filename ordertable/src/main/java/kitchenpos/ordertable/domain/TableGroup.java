package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private final List<OrderTable> orderTables;

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroup group(Long tableGroupId, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        boolean canAdd = orderTables.stream()
                .anyMatch(Predicate.not(OrderTable::canGroup));

        if (canAdd) {
            throw new IllegalArgumentException();
        }
        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));

        return new TableGroup(new ArrayList<>(orderTables));
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }

}
