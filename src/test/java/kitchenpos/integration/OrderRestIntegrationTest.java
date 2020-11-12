package kitchenpos.integration;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import kitchenpos.application.OrderService;
import kitchenpos.application.TableService;
import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

class OrderRestIntegrationTest extends IntegrationTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;

    @DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        tableService.changeEmpty(1L, CHANGING_NOT_EMPTY_ORDER_TABLE);
        tableService.changeNumberOfGuests(1L, CHANGING_GUEST_ORDER_TABLE);

        OrderLineItem orderLineItem = TestObjectUtils.createOrderLineItem(1L, null, 1L, 1L);
        Order createdOrder = TestObjectUtils.createOrder(
                null, 1L, null, null, Collections.singletonList(orderLineItem));
        String createOrderJson = objectMapper.writeValueAsString(createdOrder);

        mockMvc.perform(
                post("/api/orders")
                        .content(createOrderJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("orderTableId").exists())
                .andExpect(jsonPath("orderStatus").value(OrderStatus.COOKING.name()))
                .andExpect(jsonPath("orderedTime").exists())
                .andExpect(jsonPath("orderLineItems[0].seq").exists())
                .andExpect(jsonPath("orderLineItems[0].orderId").exists())
                .andExpect(jsonPath("orderLineItems[0].menuId").value(
                        createdOrder.getOrderLineItems().get(0).getMenuId()))
                .andExpect(jsonPath("orderLineItems[0].quantity").value(
                        createdOrder.getOrderLineItems().get(0).getQuantity()));
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        tableService.changeEmpty(1L, CHANGING_NOT_EMPTY_ORDER_TABLE);
        tableService.changeNumberOfGuests(1L, CHANGING_GUEST_ORDER_TABLE);
        OrderLineItem orderLineItem = TestObjectUtils.createOrderLineItem(1L, null, 1L, 1L);
        orderService.create(TestObjectUtils.createOrder(
                null, 1L, null, null, Collections.singletonList(orderLineItem)));

        mockMvc.perform(
                get("/api/orders")
        )
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusTest() throws Exception {
        tableService.changeEmpty(1L, CHANGING_NOT_EMPTY_ORDER_TABLE);
        tableService.changeNumberOfGuests(1L, CHANGING_GUEST_ORDER_TABLE);
        OrderLineItem orderLineItem = TestObjectUtils.createOrderLineItem(1L, null, 1L, 1L);
        Order order = TestObjectUtils.createOrder(
                null, 1L, null, null, Collections.singletonList(orderLineItem));
        Order changedOrder = orderService.create(order);
        Order changingOrder = TestObjectUtils.createOrder(null, null, OrderStatus.MEAL.name(), null,
                null);
        String changingOrderJson = objectMapper.writeValueAsString(changingOrder);

        mockMvc.perform(
                put("/api/orders/{orderId}/order-status", changedOrder.getId())
                        .content(changingOrderJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus").value(OrderStatus.MEAL.name()));
    }
}