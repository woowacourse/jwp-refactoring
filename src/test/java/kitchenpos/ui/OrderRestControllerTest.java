package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.ui.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        final long orderId = 1L;
        final OrderRequest request = new OrderRequest(1L, Collections.singletonList(new OrderLineItemsRequest(1L, 2)));
        final OrderResponse response = new OrderResponse(1L, new OrderTableResponse(1L, 10, false),
                OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItemResponse(1L, 1L, 1)));

        when(orderService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + orderId))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        final List<OrderResponse> response = Collections.singletonList(new OrderResponse(1L, new OrderTableResponse(1L, 10, false),
                OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItemResponse(1L, 1L, 1))));

        when(orderService.list()).thenReturn(response);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() throws Exception {
        final Long orderId = 1L;
        final OrderStatusRequest request = new OrderStatusRequest(OrderStatus.MEAL.name());
        final OrderResponse response = new OrderResponse(1L, new OrderTableResponse(1L, 10, false),
                OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItemResponse(1L, 1L, 1)));

        when(orderService.changeOrderStatus(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
