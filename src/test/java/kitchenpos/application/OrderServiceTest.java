package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest extends IntegrationTest {

    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);

        Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void list() {
        // given
        createOrder();

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
    }

    private Order createOrder() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);
        return orderService.create(order);
    }

    @Test
    void changeOrderStatus() {
        // given
        Order createdOrder = createOrder();
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());

        // when
        Order changedOrder = orderService.changeOrderStatus(createdOrder.getId(), order);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }
}