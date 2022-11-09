package kitchenpos.table.application.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

@Component
public class TableGroupValidatorImpl implements TableGroupValidator {

    @Override
    public void validate(final TableGroup tableGroup) {
        validateOrderTablesSize(tableGroup.getOrderTables());
        validateOrderTablesCanGroup(tableGroup.getOrderTables());
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("order tables is empty or under size");
        }
    }

    private void validateOrderTablesCanGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateCanBeGrouped();
        }
    }
}
