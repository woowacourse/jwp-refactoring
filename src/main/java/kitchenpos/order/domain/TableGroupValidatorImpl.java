package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidatorImpl implements TableGroupValidator {

    @Override
    public void validate(final TableGroup tableGroup) {
        validateOrderTables(tableGroup.getOrderTables());
    }

    public void validateOrderTables(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesCanGroup(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesCanGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateCanBeGrouped();
        }
    }
}
