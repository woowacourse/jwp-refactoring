package kitchenpos.application.integration;

import common.domain.Money;
import common.domain.Quantity;
import order.domain.Order;
import order.domain.OrderLineItems;
import order.domain.OrderStatus;
import order.dto.ChangeOrderStatusRequest;
import order.dto.CreateOrderRequest;
import order.dto.ListOrderResponse;
import order.dto.OrderLineItemRequest;
import order.dto.OrderResponse;
import order.exception.OrderTableNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import table.dto.CreateOrderTableRequest;
import table.dto.OrderTableResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends ApplicationIntegrationTest {

    private OrderTableResponse unOrderableOrderTable;
    private OrderTableResponse orderableOrderTable;
    private OrderLineItemRequest orderLineItem;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        unOrderableOrderTable = tableService.create(CreateOrderTableRequest.of(0, false));
        orderableOrderTable = tableService.create(CreateOrderTableRequest.of(3, true));
        orderLineItem = OrderLineItemRequest.of(createMenu("후라이드", Money.valueOf(16000L)).getId(), 1);
    }

    @Test
    void create_order() {
        //given
        final Long orderTableId = orderableOrderTable.getId();
        final List<OrderLineItemRequest> orderLineItems = List.of(orderLineItem);
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);

        //when
        final OrderResponse createdOrder = orderService.create(createOrderRequest);

        //then
        assertThat(createdOrder)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems.seq")
                .isEqualTo(createdOrder);
    }

    @Test
    void throw_when_order_with_zero_order_line_item() {
        //given
        final Long orderTableId = orderableOrderTable.getId();
        final List<OrderLineItemRequest> orderLineItems = List.of();
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .hasMessage(OrderLineItems.ORDER_LINE_ITEMS_IS_EMPTY_ERROR_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throw_when_order_with_negative_order_line_item() {
        //given
        final Long orderTableId = orderableOrderTable.getId();
        final List<OrderLineItemRequest> orderLineItems = List.of(OrderLineItemRequest.of(createMenu("후라이드", Money.valueOf(16000L)).getId(), -1));
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .hasMessage(Quantity.PRODUCT_QUANTITY_IS_UNDER_ONE_ERROR_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throw_when_order_with_empty_order_line_items() {
        //given
        final Long orderTableId = orderableOrderTable.getId();
        final List<OrderLineItemRequest> orderLineItems = List.of(OrderLineItemRequest.of(createMenu("후라이드", Money.valueOf(16000L)).getId(), 0));
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .hasMessage(Quantity.PRODUCT_QUANTITY_IS_UNDER_ONE_ERROR_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throw_when_order_with_invalid_order_table_id() {
        //given
        final Long orderTableId = 100L;
        final List<OrderLineItemRequest> orderLineItems = List.of(orderLineItem);
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);

        //when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(OrderTableNotFoundException.class);

    }

    @Test
    void list_orders() {
        //given
        final Long orderTableId = orderableOrderTable.getId();
        final List<OrderLineItemRequest> orderLineItems = List.of(orderLineItem);
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);
        orderService.create(createOrderRequest);

        //when
        final ListOrderResponse orders = orderService.list();

        //then
        assertThat(orders.getOrders()).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(final OrderStatus changeOrderStatus) {
        //given
        final Long orderTableId = orderableOrderTable.getId();
        final List<OrderLineItemRequest> orderLineItems = List.of(orderLineItem);
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);
        final OrderResponse createdOrder = orderService.create(createOrderRequest);

        //when
        final OrderResponse changedOrder = orderService.changeOrderStatus(createdOrder.getId(), ChangeOrderStatusRequest.of(changeOrderStatus));

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(changeOrderStatus.toString());
    }

    @Test
    void throw_when_order_status_with_completed_status() {
        //given
        final Long orderTableId = orderableOrderTable.getId();
        final List<OrderLineItemRequest> orderLineItems = List.of(orderLineItem);
        final CreateOrderRequest createOrderRequest = CreateOrderRequest.of(orderTableId, orderLineItems);
        final OrderResponse createdOrder = orderService.create(createOrderRequest);
        orderService.changeOrderStatus(createdOrder.getId(), ChangeOrderStatusRequest.of(OrderStatus.COMPLETION));

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(createdOrder.getId(), ChangeOrderStatusRequest.of(OrderStatus.MEAL)))
                .hasMessage(Order.CHANGE_COMPLETED_ORDER_STATUS_ERROR_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }
}