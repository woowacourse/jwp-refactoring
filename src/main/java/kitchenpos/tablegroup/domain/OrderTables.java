package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.GroupTableNotEnoughException;
import kitchenpos.ordertable.exception.GroupedTableNotEmptyException;
import kitchenpos.tablegroup.exception.TableAlreadyGroupedException;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private static final int TABLE_GROUP_MIN_SIZE = 2;

    private List<OrderTable> values;

    public OrderTables(List<OrderTable> values) {
        validateTableGroupSize(values);
        this.values = values;
    }

    public static OrderTables forUnGrouping(List<OrderTable> values) {
        return new OrderTables(values);
    }

    public static OrderTables forGrouping(List<OrderTable> values) {
        validateOrderTableEmptiness(values);
        validateAlreadyHasTableGroup(values);
        return new OrderTables(values);
    }

    private void validateTableGroupSize(List<OrderTable> values) {
        if (CollectionUtils.isEmpty(values) || values.size() < TABLE_GROUP_MIN_SIZE) {
            throw new GroupTableNotEnoughException();
        }
    }

    private static void validateOrderTableEmptiness(List<OrderTable> values) {
        boolean isAllTableEmpty = values.stream()
                .allMatch(OrderTable::isEmpty);
        if (!isAllTableEmpty) {
            throw new GroupedTableNotEmptyException();
        }
    }

    private static void validateAlreadyHasTableGroup(List<OrderTable> values) {
        boolean atLeastOneHasTableGroup = values.stream()
                .anyMatch(OrderTable::hasTableGroup);
        if (atLeastOneHasTableGroup) {
            throw new TableAlreadyGroupedException();
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }

    public void group(Long tableGroupId) {
        values.forEach(orderTable -> orderTable.group(tableGroupId));
    }

    public void ungroup() {
        values.forEach(OrderTable::ungroup);
    }

    public void setEmpty() {
        values.forEach(orderTable -> orderTable.setEmpty(true));
    }
}
