package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("Order를 생성한다.")
    void create() throws Exception {
        OrderResponse orderResponse = createOrderResponse(1L);
        given(orderService.create(any(OrderCommand.class))).willReturn(orderResponse);

        OrderRequest orderRequest = new OrderRequest(1L, List.of(new OrderLineItemRequest(1L, 10),
                new OrderLineItemRequest(2L, 10)));
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/orders/1"));
    }

    @Test
    @DisplayName("Order를 모두 조회한다.")
    void list() throws Exception {
        Order order1 = createOrder(1L);
        Order order2 = createOrder(2L);
        List<Order> orders = List.of(order1, order2);

        given(orderService.list()).willReturn(orders);
        mockMvc.perform(get("/api/orders"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(orders)));
    }

    @Test
    @DisplayName("Order의 상태를 변경한다.")
    void changeOrderStatus() throws Exception {
        Order order = createOrder(1L);

        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(order);
        mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(order)));
    }

    private Order createOrder(final Long id) {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2);
        Order order = new Order(id, 1L, OrderStatus.COOKING, LocalDateTime.now(),
                List.of(orderLineItem1, orderLineItem2));
        return order;
    }

    private OrderResponse createOrderResponse(final Long id) {
        OrderLineItemResponse orderLineItem1 = new OrderLineItemResponse(1L, 1L, 1L, 10);
        OrderLineItemResponse orderLineItem2 = new OrderLineItemResponse(2L, 1L, 2L, 3);
        return new OrderResponse(id, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                List.of(orderLineItem1, orderLineItem2));
    }
}
