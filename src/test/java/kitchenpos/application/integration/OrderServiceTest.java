package kitchenpos.application.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends ApplicationIntegrationTest {

    private OrderTable emptyOrderTable;
    private OrderTable noneEmptyOrderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        emptyOrderTable = tableService.create(new OrderTable(0, true));
        noneEmptyOrderTable = tableService.create(new OrderTable(0, false));
        final Menu menu = createMenu("후라이드", BigDecimal.valueOf(16000));
        orderLineItem = new OrderLineItem(menu.getId(), 1);
    }

    @Test
    void create_order() {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = new Order(orderTableId, orderLineItems);

        //when
        final Order createdOrder = orderService.create(order);

        //then
        assertThat(createdOrder)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems.seq")
                .isEqualTo(order);
    }

    @Test
    void cannot_create_order_with_zero_order_line_item() {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, 0));
        final Order order = new Order(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_order_with_negative_order_line_item() {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, -1));
        final Order order = new Order(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_order_with_empty_order_line_items() {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of();
        final Order order = new Order(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_order_with_invalid_order_line_items() {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 1));
        final Order order = new Order(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_order_with_empty_order_table_id() {
        //given
        final Long orderTableId = null;
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = new Order(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_order_with_invalid_order_table_id() {
        //given
        final Long orderTableId = 100L;
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = new Order(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_order_with_empty_order_table() {
        //given
        final Long orderTableId = emptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = new Order(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list_orders() {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order;
        final Order createdOrder = orderService.create(new Order(orderTableId, orderLineItems));

        //when
        final List<Order> orders = orderService.list();

        //then
        assertThat(orders)
                .hasSize(1)
                .extracting("id")
                .containsExactlyInAnyOrder(createdOrder.getId());
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING,MEAL", "MEAL,COMPLETION"}, delimiter = ',')
    void changeOrderStatus(final OrderStatus orderStatus, final OrderStatus changeOrderStatus) {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = orderService.create(new Order(orderTableId, orderStatus.name(), orderLineItems));

        //when
        final Order changedOrder = orderService.changeOrderStatus(order.getId(), new Order(orderTableId, changeOrderStatus.name(), orderLineItems));

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(changeOrderStatus.name());
    }

    @Test
    void cannot_change_order_status_with_completed_status() {
        //given
        final Long orderTableId = noneEmptyOrderTable.getId();
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = orderService.create(new Order(orderTableId, orderLineItems));
        orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.COMPLETION.name()));

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order(orderTableId, orderLineItems)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}