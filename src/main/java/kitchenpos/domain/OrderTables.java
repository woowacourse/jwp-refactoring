package kitchenpos.domain;

import java.util.List;
import kitchenpos.exception.GroupTableNotEnoughException;
import kitchenpos.exception.GroupedTableNotEmptyException;
import kitchenpos.exception.TableAlreadyGroupedException;
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

    public void group(TableGroup tableGroup) {
        values.forEach(orderTable -> orderTable.setTableGroup(tableGroup));
    }

    public void ungroup() {
        validateOrderStatus();
        values.forEach(OrderTable::ungroup);
    }

    private void validateOrderStatus() {
        boolean notCompletion = values.stream()
                .anyMatch(OrderTable::isNotCompletionOrderTable);
        if (notCompletion) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블이 포함된 Table Group은 그룹 해제 할 수 없습니다.");
        }
    }

    public void setEmpty() {
        values.forEach(orderTable -> orderTable.setEmpty(true));
    }
}
