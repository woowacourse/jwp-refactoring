package kitchenpos.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderLineItemResponse;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.application.dto.OrderLineItemsRequest;
import kitchenpos.order.application.OrderRequest;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("POST /api/orders")
    void createOrder() throws Exception {
        final List<OrderLineItemsRequest> orderLineItemsRequests = List.of(
                new OrderLineItemsRequest(1L, 4L),
                new OrderLineItemsRequest(2L, 7L)
        );
        final OrderRequest orderRequest = new OrderRequest(1L, orderLineItemsRequests);

        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 4L);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 7L);

        when(orderService.create(1L, orderLineItemsRequests))
                .thenReturn(
                        OrderResponse.from(new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
                                List.of(orderLineItem, orderLineItem2))
                        ));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /api/orders/{orderId}/order-status")
    void changeStatusOfOrder() throws Exception {
        final OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse(1L, 3L, 3L);
        final OrderLineItemResponse orderLineItemResponse2 = new OrderLineItemResponse(2L, 3L, 3L);
        final OrderResponse orderResponse = new OrderResponse(3L, 3L, OrderStatus.MEAL, LocalDateTime.now(),
                List.of(orderLineItemResponse, orderLineItemResponse2));
        when(orderService.changeOrderStatus(3L)).thenReturn(orderResponse);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("orderStatus", "MEAL"))))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderResponse)))
                .andDo(print());

    }
}
