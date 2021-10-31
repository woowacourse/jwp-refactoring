package kitchenpos.domain.ordertable;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.exception.InvalidStateException;

public class OrderTablesToCreateGroup {

    private final List<OrderTable> orderTables;

    public OrderTablesToCreateGroup(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        validateMinSize(orderTables);
        validateEachOrderTables(orderTables);
    }

    private void validateMinSize(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new InvalidStateException("OrderTable들의 개수가 2 이상이어야 합니다.");
        }
    }

    private void validateEachOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateEmpty();
            orderTable.validateTableGroupIsNull();
        }
    }

    public void changeAllEmptyToFalse() {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
        }
    }

    public void assign(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.assign(tableGroup);
        }
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }
}
