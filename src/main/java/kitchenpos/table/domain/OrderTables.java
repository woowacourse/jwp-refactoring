package kitchenpos.table.domain;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorType;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void registerTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            validateRegisterTableGroupPossible(orderTable);
            orderTable.registerTableGroup(tableGroup);
            orderTable.makeNotEmpty();
        }
    }

    private void validateRegisterTableGroupPossible(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new BadRequestException(ErrorType.TABLE_NOT_EMPTY_ERROR);
        }
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new BadRequestException(ErrorType.TABLE_GROUP_NOT_NULL);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void checkSameSize(int size) {
        if (size != orderTables.size()) {
            throw new BadRequestException(ErrorType.TABLE_GROUP_DUPLICATED);
        }
    }
}
