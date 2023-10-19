package kitchenpos.api.order;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.response.OrderLineItemResponse;
import kitchenpos.ui.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderListApiTest extends ApiTestConfig {

    @DisplayName("주문 전체 조회 API 테스트")
    @Test
    void listOrder() throws Exception {
        // given
        final OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse(1L, 2L, 1L);
        final OrderResponse response = new OrderResponse(1L, OrderStatus.MEAL, LocalDateTime.now(), 1L, List.of(orderLineItemResponse));

        // when
        when(orderService.list()).thenReturn(List.of(response));

        // then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(response.getId().intValue())))
                .andExpect(jsonPath("$[0].orderStatus", is(response.getOrderStatus().name())))
                .andExpect(jsonPath("$[0].orderedTime", is(response.getOrderedTime().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].orderTableId", is(response.getOrderTableId().intValue())))
                .andExpect(jsonPath("$[0].orderLineItems.size()", is(1)))
                .andExpect(jsonPath("$[0].orderLineItems[0].seq", is(response.getOrderLineItems().get(0).getSeq().intValue())))
                .andExpect(jsonPath("$[0].orderLineItems[0].menuId", is(response.getOrderLineItems().get(0).getMenuId().intValue())))
                .andExpect(jsonPath("$[0].orderLineItems[0].quantity", is(Long.valueOf(response.getOrderLineItems().get(0).getQuantity()).intValue())));
    }
}
