package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.support.RestControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.order.fixture.OrderFixture.createOrderRequest;
import static kitchenpos.order.fixture.OrderFixture.createOrderResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends RestControllerTest {

    @MockBean
    private OrderService mockOrderService;

    @DisplayName("주문 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        OrderRequest orderRequest = createOrderRequest();
        OrderResponse orderResponse = createOrderResponse(1L, orderRequest);
        when(mockOrderService.create(any())).thenReturn(orderResponse);
        mockMvc.perform(post("/api/orders")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + orderResponse.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(orderResponse)));
    }

    @DisplayName("주문 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<OrderResponse> expected = Collections.singletonList(createOrderResponse(1L, createOrderRequest()));
        when(mockOrderService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @DisplayName("주문 상태 변경 요청을 처리한다.")
    @Test
    void changeOrderStatus() throws Exception {
        OrderRequest oldOrder = createOrderRequest(1L, OrderStatus.MEAL, 1L);
        OrderResponse expectedOrderResponse = createOrderResponse(1L, oldOrder);
        when(mockOrderService.changeOrderStatus(any(), any())).thenReturn(expectedOrderResponse);
        mockMvc.perform(put("/api/orders/{orderId}/order-status", expectedOrderResponse.getId())
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedOrderResponse))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOrderResponse)));
    }
}
