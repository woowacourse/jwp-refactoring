package kitchenpos.table;

import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @Test
    void 한개_이하의_테이블은_그룹할_수_없다() {
        //given
        OrderTable orderTable = new OrderTable(3, false, false);
        OrderTables orderTables = new OrderTables(List.of(orderTable));
        //when
        //then
        assertThatThrownBy(orderTables::group)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있지_않은_테이블은_그룹할_수_없다() {
        //given
        OrderTable orderTable1 = new OrderTable(3, false, false);
        OrderTable orderTable2 = new OrderTable(3, false, false);
        OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
        //when
        //then
        assertThatThrownBy(orderTables::group)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹이_있으면_그룹할_수_없다() {
        OrderTable orderTable1 = new OrderTable(3, true, false);
        OrderTable orderTable2 = new OrderTable(3, true, false);
        OrderTable orderTable3 = new OrderTable(3, true, false);

        OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
        orderTables.group();

        OrderTables cannotGroup = new OrderTables(List.of(orderTable2, orderTable3));
        assertThatThrownBy(cannotGroup::group)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 주문이_완료_상태가_아니면_그룹해제할_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable1 = new OrderTable(3, false, false);
        OrderTable orderTable2 = new OrderTable(3, false, false);
        Order order = new Order(List.of(new OrderLineItem(1L, 1L)));
        order.changeOrderStatus(orderStatus);
        orderTable1.createdOrder(order);

        OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        assertThatThrownBy(orderTables::group)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
