package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.util.Arrays;
import java.util.List;

public class TestFixture {

    public static final List<String> COOKING_OR_MEAL_STATUS = Arrays.asList(
            OrderStatus.COOKING.name(), OrderStatus.MEAL.name()
    );

    public static OrderTable 주문_테이블을_저장한다(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

}
