package kitchenpos.order_table.domain;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderLineItem;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static kitchenpos.TestObjectFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table_group.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable orderTable = createOrderTable(true);

        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("[예외] 그룹에 포함된 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInGroup() {
        OrderTable orderTable = createOrderTable(true);

        OrderTable orderTable2 = createOrderTable(true);
        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        createTableGroup(orderTables);

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("[예외] TableGroup.addOrderTable을 통하지 않은 groupBy 접근")
    @Test
    void groupBy_Fail_NotThrough_TableGroup() {
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = createOrderTable(true);

        assertThatThrownBy(
            () -> orderTable.groupBy(tableGroup)
        ).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("[예외] TableGroup.nugroup를 통하지 않은 ungroup 접근")
    @Test
    void ungroup() {
        OrderTable orderTable = createOrderTable(true);
        OrderTable orderTable2 = createOrderTable(true);
        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        createTableGroup(orderTables);

        assertThatThrownBy(
            orderTable::ungroup
        ).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTable(false);

        orderTable.changeNumberOfGuests(10);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("[예외] 0보다 작은 수로 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_InvalidNumberOfGuest() {
        OrderTable orderTable = createOrderTable(false);

        assertThatThrownBy(
            () -> orderTable.changeNumberOfGuests(-1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_EmptyTable() {
        OrderTable orderTable = createOrderTable(true);

        assertThatThrownBy(
            () -> orderTable.changeNumberOfGuests(10)
        ).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("조리, 식사 중인 테이블일 경우 확인")
    @Test
    void isInProgress_True() {
        OrderTable orderTable = createOrderTable(false);
        OrderLineItem orderLineItem = createOrderLineItem();
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        createOrder(orderTable, orderLineItems);

        assertThat(orderTable.isInProgress()).isTrue();
    }

    @DisplayName("완료된 테이블일 경우 확인")
    @Test
    void isInProgress_False() {
        OrderTable orderTable = createOrderTable(false);
        OrderLineItem orderLineItem = createOrderLineItem();
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        Order order = createOrder(orderTable, orderLineItems);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(orderTable.isInProgress()).isFalse();
    }
}