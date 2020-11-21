package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class GroupTables {
    private static final int MIN_SIZE = 2;

    private List<OrderTable> orderTables;

    public GroupTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_SIZE) {
            throw new IllegalArgumentException();
        }
        this.orderTables = new ArrayList<>(orderTables);
    }

    public void designateGroup(Long newTableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.designateGroup(newTableGroupId);
        }
    }

    public void ungroup(TableOrderEmptyValidator tableOrderEmptyValidator) {
        tableOrderEmptyValidator.validate(getTableIds());
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private List<Long> getTableIds() {
        return orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }

    public boolean hasSize(int size) {
        return orderTables.size() == size;
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}
