package kitchenpos.api.order;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.response.OrderLineItemResponse;
import kitchenpos.order.application.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderCreateApiTest extends ApiTestConfig {

    @DisplayName("주문 생성 API 테스트")
    @Test
    void createOrder() throws Exception {
        // given
        final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 1L);
        final OrderCreateRequest request = new OrderCreateRequest(1L, List.of(orderLineItemCreateRequest));

        // when
        final OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse(
                1L,
                orderLineItemCreateRequest.getQuantity(),
                orderLineItemCreateRequest.getMenuId()
        );
        final OrderResponse response = new OrderResponse(
                1L,
                OrderStatus.MEAL,
                LocalDateTime.now(),
                request.getOrderTableId(),
                List.of(orderLineItemResponse));
        when(orderService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/orders/%d", response.getId())));
    }
}
