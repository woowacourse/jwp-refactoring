package kitchenpos.fixtures;

import kitchenpos.domain.OrderTable;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

public class OderTableFixture {

    public static OrderTable 첫번째주문테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        return orderTable;
    }

    public static OrderTable  비어있는주문테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable 손님7명의주문테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(3L);
        orderTable.setNumberOfGuests(7);
        return orderTable;
    }
}
