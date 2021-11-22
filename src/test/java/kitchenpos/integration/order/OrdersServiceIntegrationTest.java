package kitchenpos.integration.order;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdersServiceIntegrationTest extends IntegrationTest {

    private OrderTable savedOrderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable(2, false);

        savedOrderTable = tableService.create(orderTable);

        orderLineItem = new OrderLineItem(2L, 1);
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        Orders orders = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders.add(Collections.singletonList(orderLineItem));

        // when
        Orders savedOrders = ordersService.create(orders);

        // then
        assertThat(savedOrders)
            .usingRecursiveComparison()
            .ignoringFields("id", "orderedTime")
            .isEqualTo(orders);
    }

    @DisplayName("주문의 주문 항목이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingMenuInOrderLineItems_Fail() {
        // given
        Orders orders = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders.add(Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(orders))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 항목에 메뉴가 하나라도 적혀있지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderLineItems_Fail() {
        // given
        Orders orders = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders.add(Collections.singletonList(new OrderLineItem(100L, 1)));

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(orders))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 테이블이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderTable_Fail() {
        // given
        Orders orders = new Orders(100L, COOKING.name(), LocalDateTime.now());
        orders.add(Collections.singletonList(orderLineItem));

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(orders))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 테이블이 비어 있으면(손님이 없으면) 등록할 수 없다.")
    @Test
    void create_NonExistingGuestsInOrderTable_Fail() {
        // given
        Orders orders = new Orders(1L, COOKING.name(), LocalDateTime.now());
        orders.add(Collections.singletonList(orderLineItem));

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(orders))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void read_Valid_Success() {
        // given
        Orders order = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        ordersService.create(order);

        // when
        List<Orders> orders = ordersService.list();

        // then
        assertThat(orders).isNotEmpty();
    }


    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus_Valid_Success() {
        // given
        Orders orders1 = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders1.add(Collections.singletonList(orderLineItem));
        Orders orders2 = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders2.add(Collections.singletonList(orderLineItem));

        Orders savedOrders1 = ordersService.create(orders1);
        Orders savedOrders2 = ordersService.create(orders2);

        savedOrders2.changeStatus(MEAL.name());

        // when
        Orders changedOrders = ordersService.changeOrderStatus(savedOrders1.getId(), savedOrders2);

        // then
        assertThat(changedOrders.getOrderStatus()).isEqualTo(savedOrders2.getOrderStatus());
    }

    @DisplayName("주문 상태는 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatus_NonExistingOrder_Fail() {
        // given
        Orders orders = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders.add(Collections.singletonList(orderLineItem));

        Orders savedOrders = ordersService.create(orders);

        savedOrders.changeStatus(MEAL.name());

        // when
        // then
        assertThatThrownBy(() -> ordersService.changeOrderStatus(Long.MAX_VALUE, savedOrders))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 `계산 완료`면 변경할 수 없다.")
    @Test
    void changeOrderStatus_InvalidOrderStatus_Fail() {
        // given
        Orders orders1 = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders1.add(Collections.singletonList(orderLineItem));
        Orders orders2 = new Orders(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        orders2.add(Collections.singletonList(orderLineItem));

        Orders savedOrders1 = ordersService.create(orders1);
        Orders savedOrders2 = ordersService.create(orders2);

        savedOrders2.changeStatus(COMPLETION.name());

        ordersService.changeOrderStatus(savedOrders1.getId(), savedOrders2);

        // when
        // then
        assertThatThrownBy(() -> ordersService
            .changeOrderStatus(savedOrders1.getId(), savedOrders2))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
