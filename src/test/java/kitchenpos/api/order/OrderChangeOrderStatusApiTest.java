package kitchenpos.api.order;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.response.OrderLineItemResponse;
import kitchenpos.ui.dto.response.OrderResponse;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderChangeOrderStatusApiTest extends ApiTestConfig {

    @DisplayName("주문 상태 변경 API 테스트")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        final Long orderId = 1L;
        final OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.MEAL);

        // when
        final OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse(1L, 2L, 1L);
        final OrderResponse response = new OrderResponse(1L, OrderStatus.MEAL, LocalDateTime.now(), 1L, List.of(orderLineItemResponse));
        when(orderService.changeOrderStatus(eq(orderId), eq(request))).thenReturn(response);

        // then
        mockMvc.perform(put("/api/orders/{id}/order-status", orderId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(response.getId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is(response.getOrderStatus().name())))
                .andExpect(jsonPath("$.orderedTime", is(response.getOrderedTime().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.orderTableId", is(response.getOrderTableId().intValue())))
                .andExpect(jsonPath("$.orderLineItems.size()", is(1)))
                .andExpect(jsonPath("$.orderLineItems[0].seq", is(response.getOrderLineItems().get(0).getSeq().intValue())))
                .andExpect(jsonPath("$.orderLineItems[0].menuId", is(response.getOrderLineItems().get(0).getMenuId().intValue())))
                .andExpect(jsonPath("$.orderLineItems[0].quantity", is(Long.valueOf(response.getOrderLineItems().get(0).getQuantity()).intValue())));
    }
}
