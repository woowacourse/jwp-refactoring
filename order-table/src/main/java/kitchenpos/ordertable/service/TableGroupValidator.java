package kitchenpos.ordertable.service;

import static kitchenpos.common.exception.ExceptionType.ALREADY_ASSIGNED_TABLE_GROUP;
import static kitchenpos.common.exception.ExceptionType.DUPLICATED_TABLES_OF_TABLE_GROUP;
import static kitchenpos.common.exception.ExceptionType.INVALID_TABLES_COUNT_OF_TABLE_GROUP;
import static kitchenpos.common.exception.ExceptionType.NOT_EMPTY_ORDER_TABLE_IN_TABLE_GROUP;
import static java.util.stream.Collectors.toSet;

import kitchenpos.common.exception.CustomException;
import java.util.List;
import java.util.Set;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import org.springframework.stereotype.Service;

@Service
public class TableGroupValidator {

    public void validate(TableGroup tableGroup) {
        validateOrderTables(tableGroup.getOrderTables());
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesDistinct(orderTables);
        validateOrderTablesHaveNoGroup(orderTables);
        validateOrderTablesEmpty(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < 2) {
            throw new CustomException(INVALID_TABLES_COUNT_OF_TABLE_GROUP);
        }
    }

    private void validateOrderTablesDistinct(List<OrderTable> orderTables) {
        Set<Long> uniqueOrderTableIds = orderTables.stream()
                                                   .map(OrderTable::getId)
                                                   .collect(toSet());

        if (orderTables.size() != uniqueOrderTableIds.size()) {
            throw new CustomException(DUPLICATED_TABLES_OF_TABLE_GROUP);
        }
    }

    private void validateOrderTablesHaveNoGroup(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> orderTable.getTableGroupId() != null)) {
            throw new CustomException(ALREADY_ASSIGNED_TABLE_GROUP);
        }
    }

    private void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new CustomException(NOT_EMPTY_ORDER_TABLE_IN_TABLE_GROUP);
        }
    }
}
