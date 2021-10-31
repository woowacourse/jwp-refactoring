package kitchenpos.fixtures;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OderTableFixture {

    public static OrderTable 첫번째주문테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        return orderTable;
    }

    public static OrderTable 첫번째주문테이블(int guest) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(guest);
        return orderTable;
    }

    public static OrderTable 비어있는주문테이블1() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable 비어있는주문테이블2() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(3L);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable 테이블그룹주문테이블1(TableGroup tableGroup) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(4L);
        orderTable.setTableGroupId(tableGroup.getId());
        return orderTable;
    }

    public static OrderTable 테이블그룹주문테이블2(TableGroup tableGroup) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(5L);
        orderTable.setTableGroupId(tableGroup.getId());
        return orderTable;
    }

    public static OrderTable 주문테이블(Long id, Long tableGroupId, Boolean empty, int guest) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setNumberOfGuests(guest);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
