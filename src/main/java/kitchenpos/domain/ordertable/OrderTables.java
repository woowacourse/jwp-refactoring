package kitchenpos.domain.ordertable;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.InvalidStateException;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables() {
        orderTables = new ArrayList<>();
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void validate() {
        validateSize();
        validateEachOrderTables();
    }

    private void validateSize() {
        if (orderTables.size() < 2) {
            throw new InvalidStateException("OrderTable들의 개수가 2 이상이어야 합니다.");
        }
    }

    private void validateEachOrderTables() {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateIsEmpty();
            orderTable.validateTableGroupIsNull();
        }
    }

    public void ungroup() {
        removeTableGroupOfAll();
        changeAllEmptyToFalse();
    }

    private void removeTableGroupOfAll() {
        for (OrderTable orderTable : orderTables) {
            orderTable.removeTableGroup();
        }
    }

    public void assign(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.assign(tableGroup);
        }
    }

    public void changeAllEmptyToFalse() {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
        }
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }
}
