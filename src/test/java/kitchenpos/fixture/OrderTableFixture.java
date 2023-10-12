package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문_테이블_A = createOrderTable(1L, false, 10);
    public static OrderTable 주문_테이블_B_EMPTY_상태 = createOrderTable(2L, true, 10);
    public static OrderTable 주문_테이블_C_EMPTY_상태 = createOrderTable(3L, true, 10);
    public static OrderTable 주문_테이블_D_NOT_EMPTY_상태 = createOrderTable(4L, false, 10);
    public static OrderTable 주문_테이블_E_NOT_EMPTY_상태 = createOrderTable(5L, false, 10);
    public static OrderTable 주문_테이블_F_손님_0명 = createOrderTable(6L, false, 0);
    public static OrderTable 주문_테이블_G_손님_음수 = createOrderTable(7L, false, -1);
    public static OrderTable 주문_테이블_H_손님_변경_5명 = createOrderTable(8L, false, 5);
    public static OrderTable 주문_테이블_I_손님_변경_10명 = createOrderTable(9L, false, 10);
    public static OrderTable 주문_테이블_J_상태_변경_EMPTY = createOrderTable(10L, true, 10);



    private static OrderTable createOrderTable(Long id, boolean empty, int numberOfGuest) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(null);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuest);
        return orderTable;
    }
}
