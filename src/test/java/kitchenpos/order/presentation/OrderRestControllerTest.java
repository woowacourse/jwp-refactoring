package kitchenpos.order.presentation;

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
import kitchenpos.order.application.dto.request.OrderCommand;
import kitchenpos.order.application.dto.request.OrderStatusCommand;
import kitchenpos.order.application.dto.response.OrderLineItemResponse;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.OrderRequest;
import kitchenpos.order.presentation.dto.OrderStatusRequest;
import kitchenpos.support.ControllerTest;
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
        OrderResponse orderResponse1 = createOrderResponse(1L);
        OrderResponse orderResponse2 = createOrderResponse(2L);
        List<OrderResponse> orderResponses = List.of(orderResponse1, orderResponse2);

        given(orderService.list()).willReturn(orderResponses);
        mockMvc.perform(get("/api/orders"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(orderResponses)));
    }

    @Test
    @DisplayName("Order의 상태를 변경한다.")
    void changeOrderStatus() throws Exception {
        OrderResponse orderResponse = createOrderResponse(1L);

        given(orderService.changeOrderStatus(anyLong(), any(OrderStatusCommand.class))).willReturn(orderResponse);
        mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequest(OrderStatus.COMPLETION.name()))))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(orderResponse)));
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
        return new OrderResponse(id, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                List.of(orderLineItem1, orderLineItem2));
    }
}
