package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("OrderTables를 단체로 지정한다.")
    @Test
    void groupBy() {
        //given
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(2, false);
        OrderTable orderTable3 = new OrderTable(3, false);

        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));
        TableGroup tableGroup = new TableGroup(1L);

        //when
        orderTables.groupBy(tableGroup);

        //then
        assertThat(orderTable1.getIdOfTableGroup()).isEqualTo(tableGroup.getId());
        assertThat(orderTable2.getIdOfTableGroup()).isEqualTo(tableGroup.getId());
        assertThat(orderTable3.getIdOfTableGroup()).isEqualTo(tableGroup.getId());
    }

    @DisplayName("OrderTables의 단체 지정을 해지한다.")
    @Test
    void ungroup() {
        //given
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(2, false);
        OrderTable orderTable3 = new OrderTable(3, false);

        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));
        TableGroup tableGroup = new TableGroup(1L);

        orderTables.groupBy(tableGroup);

        Order order1 = new Order(OrderStatus.COMPLETION);
        Order order2 = new Order(OrderStatus.COMPLETION);
        orderTable1.addOrder(order1);
        orderTable2.addOrder(order2);

        //when
        orderTables.ungroup();

        //then
        assertThat(orderTable1.getIdOfTableGroup()).isNull();
        assertThat(orderTable2.getIdOfTableGroup()).isNull();
        assertThat(orderTable3.getIdOfTableGroup()).isNull();
    }

    @DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    void ungroupException1(OrderStatus wrongOrderStatus) {
        //given
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(2, false);
        OrderTable orderTable3 = new OrderTable(3, false);

        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));
        TableGroup tableGroup = new TableGroup(1L);

        orderTables.groupBy(tableGroup);

        Order order1 = new Order(OrderStatus.COMPLETION);
        orderTable1.addOrder(order1);
        Order order2 = new Order(wrongOrderStatus);
        orderTable2.addOrder(order2);

        //then
        assertThatThrownBy(orderTables::ungroup)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없습니다.");
    }
}