package kitchenpos.application.common.factory;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFactory {

    private OrderTableFactory() {}

    public static OrderTable create(int numberOfGuests, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
